package org.miu.mppproject.librarysystem.libraryapp.data.dao;

import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.*;

import java.sql.*;
import java.util.*;

public class BookDao {
    private final DataSource dataSource;

    public BookDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Add a book if ISBN does not already exist
    public void addBook(Book book) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        String insertQuery = "INSERT INTO books (isbn, title, max_checkout_length) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, book.getIsbn());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Book with ISBN " + book.getIsbn() + " already exists.");
            }
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, book.getIsbn());
            insertStmt.setString(2, book.getTitle());
            insertStmt.setInt(3, book.getMaxCheckoutLength());
            insertStmt.executeUpdate();
        }
    }

    // Get all books including their authors and copies
    public List<Book> getAllLibraryBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT isbn, title, max_checkout_length FROM books";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                books.add(new Book(
                        isbn,
                        rs.getString("title"),
                        getBookAuthors(isbn),
                        getAllBookCopies(isbn),
                        rs.getInt("max_checkout_length")
                ));
            }
        }
        return books;
    }

    // Get all book copies of a given book including its details
    public List<BookCopy> getAllBookCopies(String isbn) throws SQLException {
        List<BookCopy> bookCopies = new ArrayList<>();

        String bookCopiesQuery = """
                SELECT bc.copy_number, bc.is_available
                FROM book_copies bc
                WHERE bc.isbn = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(bookCopiesQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BookCopy bookCopy = new BookCopy(
                            rs.getBoolean("is_available"),
                            rs.getInt("copy_number"),
                            null // Temporarily null, will be set later
                    );
                    bookCopies.add(bookCopy);
                }
            }
        }

        // Associate copies with the book
        Book book = fetchBookWithAuthors(isbn);
        if (book != null) {
            for (BookCopy copy : bookCopies) {
                copy.setBook(book);
            }
        }

        return bookCopies;
    }

    // Fetch book details including its authors
    private Book fetchBookWithAuthors(String isbn) throws SQLException {
        Book book = null;

        String bookQuery = """
                SELECT b.isbn, b.title, b.max_checkout_length
                FROM books b
                WHERE b.isbn = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(bookQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            getBookAuthors(isbn),
                            new ArrayList<>(),  // Empty, will be set later
                            rs.getInt("max_checkout_length")
                    );
                }
            }
        }
        return book;
    }

    // Fetch authors of a book
    private List<Author> getBookAuthors(String isbn) throws SQLException {
        List<Author> authors = new ArrayList<>();
        String authorQuery = """
                SELECT a.author_id, a.first_name, a.last_name, a.short_bio,
                       c.phone_number,
                       ad.street, ad.city, ad.state, ad.zip_code
                FROM book_authors ba
                JOIN authors a ON ba.author_id = a.author_id
                LEFT JOIN contact_info c ON a.author_id = c.author_id
                LEFT JOIN address ad ON c.address_id = ad.address_id
                WHERE ba.isbn = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(authorQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ContactInfo contactInfo = new ContactInfo(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("phone_number"),
                            new Address(
                                    rs.getString("street"),
                                    rs.getString("city"),
                                    rs.getString("state"),
                                    rs.getString("zip_code")
                            )
                    );

                    authors.add(new Author(
                            rs.getString("short_bio"),
                            contactInfo
                    ));
                }
            }
        }
        return authors;
    }

    // Search for book availability by ISBN including authors and copies
    public Optional<Book> searchBookAvailability(String isbn) throws SQLException {
        String query = """
                SELECT b.isbn, b.title, b.max_checkout_length
                FROM books b
                JOIN book_copies bc ON b.isbn = bc.isbn
                WHERE b.isbn = ? AND bc.is_available = true
                LIMIT 1
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            getBookAuthors(isbn),
                            getAllBookCopies(isbn),
                            rs.getInt("max_checkout_length")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    // Check which member has a specific book copy
    public Optional<Member> checkBookCopyNumberAndIsWith(String copyNumber, String isbn) throws SQLException {
        String query = """
                SELECT m.member_id, m.first_name, m.last_name, m.phone_number, m.street, m.city, m.state, m.zipcode
                FROM checkout_entries ce
                JOIN members m ON ce.member_id = m.member_id
                JOIN book_copies bc ON ce.copy_number = bc.copy_number AND ce.isbn = bc.isbn
                WHERE ce.copy_number = ? AND ce.isbn = ? AND bc.is_available = false
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, copyNumber);
            stmt.setString(2, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Member(
                            new ContactInfo(
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("phone_number"),
                                    new Address(rs.getString("street"),
                                            rs.getString("city"),
                                            rs.getString("state"),
                                            rs.getString("zipcode")
                                    ))
                    ));
                }
            }
        }
        return Optional.empty();
    }
}
