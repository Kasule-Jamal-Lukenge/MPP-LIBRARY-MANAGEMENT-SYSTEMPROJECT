package dataaccess.dao;

import business.Author;
import business.Book;
import business.BookCopy;
import business.Address;
import dataaccess.storage.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class BookDAO implements BaseDAO<Book, String> {

    @Override
    public void save(Book newBook) {
        String bookQuery = "INSERT INTO book (isbn, title, max_checkout_length) VALUES (?, ?, ?);";
        String addressQuery = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?);";
        String authorQuery = "INSERT INTO author (firstName, lastName, telephone, bio, address_id) VALUES (?, ?, ?, ?, ?)";
        String bookAuthorQuery = "INSERT INTO book_author (book_isbn, author_id) VALUES (?, ?) ON CONFLICT DO NOTHING;";
        String bookCopyQuery = "INSERT INTO book_copy (book_isbn, copy_num, is_available) VALUES (?, ?, ?) ON CONFLICT DO NOTHING;";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Insert Book
            try (PreparedStatement bookStmt = conn.prepareStatement(bookQuery)) {
                bookStmt.setString(1, newBook.getIsbn());
                bookStmt.setString(2, newBook.getTitle());
                bookStmt.setInt(3, newBook.getMaxCheckoutLength());
                bookStmt.executeUpdate();
            }

            // Insert Authors
            for (Author author : newBook.getAuthors()) {
                int addressId = -1;
                int authorId = -1;

                // Insert Address
                try (PreparedStatement addressStmt = conn.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS)) {
                    Address address = author.getAddress();
                    addressStmt.setString(1, address.getStreet());
                    addressStmt.setString(2, address.getCity());
                    addressStmt.setString(3, address.getState());
                    addressStmt.setString(4, address.getZip());
                    addressStmt.executeUpdate();

                    try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            addressId = generatedKeys.getInt(1);
                        }
                    }
                }

                // Insert Author
                if (addressId != -1) {
                    try (PreparedStatement authorStmt = conn.prepareStatement(authorQuery, Statement.RETURN_GENERATED_KEYS)) {
                        authorStmt.setString(1, author.getFirstName());
                        authorStmt.setString(2, author.getLastName());
                        authorStmt.setString(3, author.getTelephone());
                        authorStmt.setString(4, author.getBio());
                        authorStmt.setInt(5, addressId);
                        authorStmt.executeUpdate();

                        try (ResultSet generatedKeys = authorStmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                authorId = generatedKeys.getInt(1);
                            }
                        }
                    }
                }

                // Link Book to Author
                if (authorId != -1) {
                    try (PreparedStatement bookAuthorStmt = conn.prepareStatement(bookAuthorQuery)) {
                        bookAuthorStmt.setString(1, newBook.getIsbn());
                        bookAuthorStmt.setInt(2, authorId);
                        bookAuthorStmt.executeUpdate();
                    }
                }
            }

            // Insert Book Copies
            for (BookCopy copy : newBook.getCopies()) {
                try (PreparedStatement bookCopyStmt = conn.prepareStatement(bookCopyQuery)) {
                    bookCopyStmt.setString(1, newBook.getIsbn());
                    bookCopyStmt.setInt(2, copy.getCopyNum());
                    bookCopyStmt.setBoolean(3, copy.isAvailable());
                    bookCopyStmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //readBooksMap
    public HashMap<String, Book> getAllBooks() {
        HashMap<String, Book> books = new HashMap<>();
        String query = "SELECT b.isbn, b.title, b.max_checkout_length, " +
                "       a.authorId, a.firstName AS author_first_name, a.lastName AS author_last_name, a.bio " +
                "FROM book b " +
                "LEFT JOIN book_author ba ON b.isbn = ba.book_isbn " +
                "LEFT JOIN author a ON ba.author_id = a.authorId";

        String copiesQuery = "SELECT bc.copy_num, bc.is_available FROM book_copy bc WHERE bc.book_isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            HashMap<String, List<Author>> authorsByIsbn = new HashMap<>();

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                int maxCheckoutLength = rs.getInt("max_checkout_length");

                // Collect authors
                if (rs.getInt("authorId") != 0) {
                    String authorFirstName = rs.getString("author_first_name");
                    String authorLastName = rs.getString("author_last_name");
                    String authorBio = rs.getString("bio");
                    Author author = new Author(authorFirstName, authorLastName, null, null, authorBio);

                    authorsByIsbn.putIfAbsent(isbn, new ArrayList<>());
                    if (!authorsByIsbn.get(isbn).contains(author)) {
                        authorsByIsbn.get(isbn).add(author);
                    }
                }

                // Ensure the book exists in the map
                books.putIfAbsent(isbn, new Book(isbn, title, maxCheckoutLength, authorsByIsbn.getOrDefault(isbn, new ArrayList<>())));
            }

            // Now, fetch the book copies for each book
            for (Map.Entry<String, Book> entry : books.entrySet()) {
                String isbn = entry.getKey();
                Book book = entry.getValue();

                book.clearCopies(); // Ensure the list is empty before adding copies

                try (PreparedStatement pstmt = conn.prepareStatement(copiesQuery)) {
                    pstmt.setString(1, isbn);
                    try (ResultSet copyRs = pstmt.executeQuery()) {
                        while (copyRs.next()) {
                            int copyNum = copyRs.getInt("copy_num");
                            boolean isAvailable = copyRs.getBoolean("is_available");
                            BookCopy bookCopy = new BookCopy(book, copyNum, isAvailable);
                            book.addCopy(bookCopy);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void addBookCopy(BookCopy copy) {
        String query = "INSERT INTO book_copy (book_isbn, copy_num, is_available) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, copy.getBook().getIsbn());
            pstmt.setInt(2, copy.getCopyNum());
            pstmt.setBoolean(3, copy.isAvailable());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBooks(List<Book> bookList) {
        List<Book> bookListTest = bookList;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String addressSql = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET street = EXCLUDED.street, city = EXCLUDED.city, " +
                    "state = EXCLUDED.state, zip = EXCLUDED.zip RETURNING id";

            String authorSql = "INSERT INTO author (firstName, lastName, telephone, address_id, bio) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (authorId) DO UPDATE SET firstName = EXCLUDED.firstName, " +
                    "lastName = EXCLUDED.lastName, telephone = EXCLUDED.telephone, " +
                    "address_id = EXCLUDED.address_id, bio = EXCLUDED.bio";

            String bookSql = "INSERT INTO book (isbn, title, max_checkout_length) VALUES (?, ?, ?) " +
                    "ON CONFLICT (isbn) DO UPDATE SET title = EXCLUDED.title, max_checkout_length = EXCLUDED.max_checkout_length";

            String bookAuthorSql = "INSERT INTO book_author (book_isbn, author_id) VALUES (?, ?) " +
                    "ON CONFLICT DO NOTHING";
            // Insert book copy SQL
            String bookCopySql = "INSERT INTO book_copy (book_isbn, copy_num, is_available) VALUES (?, ?, ?)";

            try (PreparedStatement addressStmt = conn.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement authorStmt = conn.prepareStatement(authorSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement bookStmt = conn.prepareStatement(bookSql);
                 PreparedStatement bookAuthorStmt = conn.prepareStatement(bookAuthorSql);
                 PreparedStatement bookCopyStmt = conn.prepareStatement(bookCopySql)) {

                for (Book book : bookList) {
                    Map<Author, Integer> authorIdMap = new HashMap<>(); // Store authorId for each author
                    for (Author author : book.getAuthors()) {
                        // Insert address first
                        Address address = author.getAddress(); // Assuming Author has an Address object
                        int addressId = -1;


                        if (address != null) {
                            addressStmt.setString(1, address.getStreet());
                            addressStmt.setString(2, address.getCity());
                            addressStmt.setString(3, address.getState());
                            addressStmt.setString(4, address.getZip());
                            addressStmt.executeUpdate();

                            ResultSet rs = addressStmt.getGeneratedKeys();
                            if (rs.next()) {
                                addressId = rs.getInt(1);
                            }
                        }

                        // Insert author
                        authorStmt.setString(1, author.getFirstName());
                        authorStmt.setString(2, author.getLastName());
                        authorStmt.setString(3, author.getTelephone());
                        authorStmt.setInt(4, addressId); // Use the generated address ID
                        authorStmt.setString(5, author.getBio());
                        authorStmt.executeUpdate();

                        ResultSet rs2 = authorStmt.getGeneratedKeys();
                        if (rs2.next()) {
                            int authorId = rs2.getInt(1);
                            authorIdMap.put(author, authorId); // Store correct authorId
                        }
                    }

                    // Insert book
                    bookStmt.setString(1, book.getIsbn());
                    bookStmt.setString(2, book.getTitle());
                    bookStmt.setInt(3, book.getMaxCheckoutLength());
                    bookStmt.executeUpdate();

                    // Insert into book_author table
                    for (Author author : book.getAuthors()) {
                        Integer authorId = authorIdMap.get(author);
                        bookAuthorStmt.setString(1, book.getIsbn());
                        bookAuthorStmt.setInt(2, authorId);
                        bookAuthorStmt.executeUpdate();
                    }
                    // Insert copies into book_copy table
                    int numCopies = book.getNumCopies(); // Get the number of copies from the book object
                    for (int copyNum = 1; copyNum <= numCopies; copyNum++) {
                        bookCopyStmt.setString(1, book.getIsbn()); // Set book ISBN
                        bookCopyStmt.setInt(2, copyNum); // Set copy number
                        bookCopyStmt.setBoolean(3, true); // Set the copy as available initially
                        bookCopyStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<Book> findById(String isbn) {
        return Optional.empty(); // Implementation needed
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(); // Implementation needed
    }

    @Override
    public void update(Book book) {
        // Implementation needed
    }

    @Override
    public void delete(String isbn) {
        // Implementation needed
    }
}

