package org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public final class LibraryDataSource implements DataSource {
    private final Connection connection;

    @Inject
    public LibraryDataSource(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    @Override
    public void initializeDatabase() {
        createTables();
    }

    private void createTables() {
        String sql = """
            
                -- Users Table (Extends Person)
            CREATE TABLE User (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                username VARCHAR(100) UNIQUE NOT NULL,
                contact_info_id CHAR(36) NOT NULL,
                role_id CHAR(36) NOT NULL,
                FOREIGN KEY (contact_info_id) REFERENCES ContactInfo(id) ON DELETE CASCADE,
                FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE SET NULL
            );
            
            -- Role Table
            CREATE TABLE Role (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                name VARCHAR(100) UNIQUE NOT NULL
            );
            
            -- Permissions Table
            CREATE TABLE Permission (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                action_name VARCHAR(100) NOT NULL UNIQUE
            );
            
            -- Many-to-Many: Role - Permissions
            CREATE TABLE Role_Permissions (
                role_id CHAR(36) NOT NULL,
                permission_id CHAR(36) NOT NULL,
                PRIMARY KEY (role_id, permission_id),
                FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE,
                FOREIGN KEY (permission_id) REFERENCES Permission(id) ON DELETE CASCADE
            );
            
            -- Library Members (Extends Person)
            CREATE TABLE Member (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                contact_info_id CHAR(36) NOT NULL,
                FOREIGN KEY (contact_info_id) REFERENCES ContactInfo(id) ON DELETE CASCADE
            );
            
            -- Authors Table
            CREATE TABLE Author (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                short_bio TEXT NOT NULL,
                contact_info_id CHAR(36) NOT NULL,
                FOREIGN KEY (contact_info_id) REFERENCES ContactInfo(id) ON DELETE CASCADE
            );
            
            -- Books Table
            CREATE TABLE Book (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                isbn VARCHAR(20) UNIQUE NOT NULL,
                title VARCHAR(255) NOT NULL,
                max_checkout_length INT NOT NULL
            );
            
            -- Many-to-Many: Book - Authors
            CREATE TABLE Book_Authors (
                book_id CHAR(36) NOT NULL,
                author_id CHAR(36) NOT NULL,
                PRIMARY KEY (book_id, author_id),
                FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE,
                FOREIGN KEY (author_id) REFERENCES Author(id) ON DELETE CASCADE
            );
            
            -- Book Copies Table
            CREATE TABLE BookCopy (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                book_id CHAR(36) NOT NULL,
                copy_number INT NOT NULL,
                is_available BOOLEAN DEFAULT TRUE,
                FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
            );
            
            -- Checkout Entry Table
            CREATE TABLE CheckoutEntry (
                id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                book_copy_id CHAR(36) NOT NULL,
                member_id CHAR(36) NOT NULL,
                checkout_date DATE NOT NULL,
                due_date DATE NOT NULL,
                date_returned DATE,
                fine_amount DECIMAL(10,2) DEFAULT 0.00,
                FOREIGN KEY (book_copy_id) REFERENCES BookCopy(id) ON DELETE CASCADE,
                FOREIGN KEY (member_id) REFERENCES Member(id) ON DELETE CASCADE
            );
            
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();  // Handle error properly
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

