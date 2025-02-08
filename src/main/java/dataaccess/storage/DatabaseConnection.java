package dataaccess.storage;

import dataaccess.DataAccessFacade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";
    private static Connection connection;

    static {
            initializeDatabase();
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reconnecting to the database", e);
        }
        return connection;
    }

    private static Connection createConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database connection", e);
        }
    }


    private static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Books Table
            stmt.execute("CREATE TABLE IF NOT EXISTS book ( \n" +
                    "    isbn VARCHAR(20) PRIMARY KEY, \n" +
                    "    title VARCHAR(255), \n" +
                    "    max_checkout_length INT \n" +
                    ");");

            // Address Table
            stmt.execute("CREATE TABLE IF NOT EXISTS address ( \n" +
                    "    id SERIAL PRIMARY KEY, \n" +
                    "    street VARCHAR(255), \n" +
                    "    city VARCHAR(255), \n" +
                    "    state VARCHAR(255), \n" +
                    "    zip VARCHAR(10) \n" +
                    ");");

            // Library Membership Table
            // Members Table
            stmt.execute("CREATE TABLE IF NOT EXISTS member ( \n" +
                    "    memberId VARCHAR(10) NOT NULL PRIMARY KEY, \n" +
                    "    firstName VARCHAR(255) NOT NULL, \n" +
                    "    lastName VARCHAR(255) NOT NULL, \n" +
                    "    telephone VARCHAR(20), \n" +
                    "    address_id INT NOT NULL, \n" +
                    "    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE \n" +
                    ");");

            // Users Table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (\n" +
                    "    id VARCHAR(10) PRIMARY KEY, \n" +
                    "    password VARCHAR(255), \n" +
                    "    auth_level VARCHAR(20) NOT NULL CHECK (auth_level IN ('LIBRARIAN', 'ADMIN', 'BOTH'))\n" +
                    ");");

            // Book Copies Table
            stmt.execute("CREATE TABLE IF NOT EXISTS book_copy ( \n" +
                    "    id SERIAL PRIMARY KEY, \n" +
                    "    book_isbn VARCHAR(20) NOT NULL, \n" +
                    "    copy_num INT NOT NULL, \n" +
                    "    is_available BOOLEAN DEFAULT TRUE, \n" +
                    "    CONSTRAINT fk_book FOREIGN KEY (book_isbn) REFERENCES book(isbn) ON DELETE CASCADE \n" +
                    ");");

            // Authors Table
            stmt.execute("CREATE TABLE IF NOT EXISTS author ( \n" +
                    "    authorId SERIAL PRIMARY KEY, \n" +
                    "    firstName VARCHAR(255) NOT NULL, \n" +
                    "    lastName VARCHAR(255) NOT NULL, \n" +
                    "    telephone VARCHAR(20), \n" +
                    "    address_id INT NOT NULL, \n" +
                    "    bio VARCHAR(255),\n" +
                    "    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE \n" +
                    ");");

            // Books_Authors Table
            stmt.execute("CREATE TABLE IF NOT EXISTS book_author (\n" +
                    "    book_isbn VARCHAR(20) NOT NULL, \n" +
                    "    author_id INT NOT NULL, \n" +
                    "    CONSTRAINT fk_book_authors_book FOREIGN KEY (book_isbn) REFERENCES book(isbn) ON DELETE CASCADE, \n" +
                    "    CONSTRAINT fk_book_authors_author FOREIGN KEY (author_id) REFERENCES author(authorId) ON DELETE CASCADE\n" +
                    ");");
            // Checkout Records Table
            stmt.execute("CREATE TABLE IF NOT EXISTS checkout_record ( \n" +
                    "    id SERIAL PRIMARY KEY, \n" +
                    "    member_id VARCHAR(10) NOT NULL, \n" +
                    "    book_copy_id INT NOT NULL, \n" +
                    "    checkout_date DATE NOT NULL, \n" +
                    "    due_date DATE NOT NULL, \n" +
                    "    is_returned BOOLEAN DEFAULT FALSE, \n" +
                    "    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member(memberId) ON DELETE CASCADE, \n" +
                    "    CONSTRAINT fk_book_copy FOREIGN KEY (book_copy_id) REFERENCES book_copy(id) ON DELETE CASCADE \n" +
                    ");");  // Added the missing closing parenthesis

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


