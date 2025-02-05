package org.miu.mppproject.librarysystem.libraryapp.data.dao;

import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.utlis.exception.CheckoutException;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckoutDao {

    private final Connection connection;

    private final DataSource dataSource;

    public CheckoutDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.connection = dataSource.getConnection();
    }

    public void checkoutBook(String memberId, String copyNumber, String isbn) {
        try {
            // Check if the member exists
            if (!isMemberExists(memberId)) {
                throw new CheckoutException("Member not found");
            }

            // Check if the book copy is available
            BookCopy bookCopy = getAvailableBookCopy(copyNumber, isbn);
            if (bookCopy == null) {
                throw new CheckoutException("No available copies for the requested book");
            }

            // Insert a new checkout entry
            String insertQuery = """
                        INSERT INTO checkout_entries (entry_checkout_id, member_id, copy_number, isbn, checkout_date, due_date)
                        VALUES (?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                String entryCheckoutId = generateEntryCheckoutId();
                LocalDate checkoutDate = LocalDate.now();
                LocalDate dueDate = checkoutDate.plusDays(getMaxCheckoutLength(isbn));

                stmt.setString(1, entryCheckoutId);
                stmt.setString(2, memberId);
                stmt.setString(3, copyNumber);
                stmt.setString(4, isbn);
                stmt.setDate(5, Date.valueOf(checkoutDate));
                stmt.setDate(6, Date.valueOf(dueDate));

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new CheckoutException("Failed to insert checkout entry: " + e.getMessage());
            }

            // Mark the book copy as unavailable
            updateBookCopyAvailability(copyNumber, isbn, false);

        } catch (SQLException e) {
            throw new CheckoutException("Unable to checkout book: " + e.getMessage());
        }
    }

    private boolean isMemberExists(String memberId) throws SQLException {
        String query = "SELECT 1 FROM members WHERE member_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void updateBookCopyAvailability(String copyNumber, String isbn, boolean isAvailable) throws SQLException {
        String query = "UPDATE book_copies SET is_available = ? WHERE copy_number = ? AND isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, Integer.parseInt(copyNumber));
            stmt.setString(3, isbn);
            stmt.executeUpdate();
        }
    }

    private int getMaxCheckoutLength(String isbn) throws SQLException {
        String query = "SELECT max_checkout_length FROM books WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_checkout_length");
                }
            }
        }
        return 14; // Default 2 weeks if not found
    }

    private String generateEntryCheckoutId() {
        return "CE-" + UUID.randomUUID();
    }

    public List<CheckoutEntry> getAllCheckoutEntries() throws SQLException {
        List<CheckoutEntry> checkoutEntries = new ArrayList<>();
        String query = """
                SELECT ce.entry_checkout_id, ce.checkout_date, ce.due_date, ce.date_returned, ce.fine_amount,
                       m.member_id, m.first_name, m.last_name, m.phone_number, m.street, m.city, m.state, m.zipcode,
                       bc.copy_number, bc.is_available,
                       b.isbn, b.title, b.max_checkout_length
                FROM checkout_entries ce
                JOIN members m ON ce.member_id = m.member_id
                JOIN book_copies bc ON ce.copy_number = bc.copy_number AND ce.isbn = bc.isbn
                JOIN books b ON bc.isbn = b.isbn
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                checkoutEntries.add(mapToCheckoutEntry(rs));
            }
        }
        return checkoutEntries;
    }

    public List<CheckoutEntry> getMemberCheckoutEntries(String memberId) throws SQLException {
        List<CheckoutEntry> checkoutEntries = new ArrayList<>();
        String query = """
                SELECT ce.entry_checkout_id, ce.checkout_date, ce.due_date, ce.date_returned, ce.fine_amount,
                       m.member_id, m.first_name, m.last_name, m.phone_number, m.street, m.city, m.state, m.zipcode,
                       bc.copy_number, bc.is_available,
                       b.isbn, b.title, b.max_checkout_length
                FROM checkout_entries ce
                JOIN members m ON ce.member_id = m.member_id
                JOIN book_copies bc ON ce.copy_number = bc.copy_number AND ce.isbn = bc.isbn
                JOIN books b ON bc.isbn = b.isbn
                WHERE ce.member_id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    checkoutEntries.add(mapToCheckoutEntry(rs));
                }
            }
        }
        return checkoutEntries;
    }

    private CheckoutEntry mapToCheckoutEntry(ResultSet rs) throws SQLException {

        Member member = new Member(
                new ContactInfo(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone_number"),
                        new Address(
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getString("zipcode")
                        )
                )
        );
        member.setMemberId(rs.getString("member_id"));

        // Fetch the book (including its authors and all book copies)
        String isbn = rs.getString("isbn");
        Book book = fetchBookWithAuthorsAndCopies(isbn);

        //  Create the BookCopy and ensure it references the full Book
        BookCopy bookCopy = new BookCopy(
                rs.getBoolean("is_available"),
                rs.getInt("copy_number"),
                book
        );

        //  Create and return the fully populated CheckoutEntry
        CheckoutEntry checkoutEntry = new CheckoutEntry(
                bookCopy,
                member,
                rs.getDate("checkout_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getDate("date_returned") != null ? rs.getDate("date_returned").toLocalDate() : null,
                rs.getDouble("fine_amount")
        );

        checkoutEntry.setEntryCheckoutId(rs.getString("entry_checkout_id"));
        return checkoutEntry;
    }

    private BookCopy getAvailableBookCopy(String copyNumber, String isbn) throws SQLException {
        // Fetch the book (with all its book copies)
        Book book = fetchBookWithAuthorsAndCopies(isbn);

        // Find the specific book copy in the database
        String query = "SELECT copy_number, is_available FROM book_copies WHERE copy_number = ? AND isbn = ? AND is_available = true";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, copyNumber);
            stmt.setString(2, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BookCopy(
                            rs.getBoolean("is_available"),
                            rs.getInt("copy_number"),
                            book // Assign the fully populated Book instance
                    );
                }
            }
        }
        return null;
    }

    private Book fetchBookWithAuthorsAndCopies(String isbn) throws SQLException {
        Book book = null;

        //  Fetch book details
        String bookQuery = """
                    SELECT b.isbn, b.title, b.max_checkout_length
                    FROM books b
                    WHERE b.isbn = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(bookQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            getBookAuthors(isbn),
                            new ArrayList<>(),
                            rs.getInt("max_checkout_length")
                    );
                }
            }
        }

        if (book == null) return null;

        //  Fetch all book copies for this book
        String bookCopiesQuery = """
                    SELECT bc.copy_number, bc.is_available
                    FROM book_copies bc
                    WHERE bc.isbn = ?
                """;

        List<BookCopy> bookCopies = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(bookCopiesQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BookCopy bookCopy = new BookCopy(
                            rs.getBoolean("is_available"),
                            rs.getInt("copy_number"),
                            book // Ensure each book copy references the same Book instance
                    );
                    bookCopies.add(bookCopy);
                }
            }
        }

        // Step 3: Link book copies to the book
        book.setBookCopies(bookCopies);
        return book;
    }

    private List<Author> getBookAuthors(String isbn) throws SQLException {
        List<Author> authors = new ArrayList<>();
        String authorQuery = """
        SELECT a.author_id, a.first_name, a.last_name, a.short_bio, 
               a.phone_number, a.street, a.city, a.state, a.zipcode
        FROM book_authors ba
        JOIN authors a ON ba.author_id = a.author_id
        WHERE ba.isbn = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(authorQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(new Author(
                            rs.getString("short_bio"),
                            new ContactInfo(
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("phone_number"),
                                    new Address(
                                            rs.getString("street"),
                                            rs.getString("city"),
                                            rs.getString("state"),
                                            rs.getString("zipcode")
                                    )
                            )
                    ));
                }
            }
        }
        return authors;
    }
}
