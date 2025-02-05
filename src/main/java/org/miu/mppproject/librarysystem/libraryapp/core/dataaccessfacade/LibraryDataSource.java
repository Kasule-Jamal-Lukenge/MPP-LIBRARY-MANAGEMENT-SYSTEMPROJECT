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
            -- Enable pgcrypto extension for UUID generation
              CREATE EXTENSION IF NOT EXISTS "pgcrypto";
          
              -- ContactInfo Table
              CREATE TABLE IF NOT EXISTS ContactInfo (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  first_name VARCHAR(100) NOT NULL,
                  last_name VARCHAR(100) NOT NULL,
                  phone_number VARCHAR(20) NOT NULL UNIQUE,
                  address TEXT NOT NULL
              );
          
              -- Users Table (Extends Person)
              CREATE TABLE IF NOT EXISTS "User" (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  username VARCHAR(100) UNIQUE NOT NULL,
                  contact_info_id UUID NOT NULL,
                  role_id UUID NOT NULL,
                  FOREIGN KEY (contact_info_id) REFERENCES ContactInfo(id) ON DELETE CASCADE,
                  FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE SET NULL
              );
          
              -- Role Table
              CREATE TABLE IF NOT EXISTS "Role" (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  name VARCHAR(100) UNIQUE NOT NULL
              );
          
              -- Permissions Table
              CREATE TABLE IF NOT EXISTS "Permission" (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  action_name VARCHAR(100) NOT NULL UNIQUE
              );
          
              -- Many-to-Many: Role - Permissions
              CREATE TABLE IF NOT EXISTS Role_Permissions (
                  role_id UUID NOT NULL,
                  permission_id UUID NOT NULL,
                  PRIMARY KEY (role_id, permission_id),
                  FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE,
                  FOREIGN KEY (permission_id) REFERENCES Permission(id) ON DELETE CASCADE
              );
          
              -- Library Members (Extends Person)
              CREATE TABLE IF NOT EXISTS Member (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  contact_info_id UUID NOT NULL,
                  FOREIGN KEY (contact_info_id) REFERENCES ContactInfo(id) ON DELETE CASCADE
              );
          
              -- Authors Table
              CREATE TABLE IF NOT EXISTS Author (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  short_bio TEXT NOT NULL,
                  contact_info_id UUID NOT NULL,
                  FOREIGN KEY (contact_info_id) REFERENCES ContactInfo(id) ON DELETE CASCADE
              );
          
              -- Books Table
              CREATE TABLE IF NOT EXISTS Book (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  isbn VARCHAR(20) UNIQUE NOT NULL,
                  title VARCHAR(255) NOT NULL,
                  max_checkout_length INT NOT NULL
              );
          
              -- Many-to-Many: Book - Authors
              CREATE TABLE IF NOT EXISTS Book_Authors (
                  book_id UUID NOT NULL,
                  author_id UUID NOT NULL,
                  PRIMARY KEY (book_id, author_id),
                  FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE,
                  FOREIGN KEY (author_id) REFERENCES Author(id) ON DELETE CASCADE
              );
          
              -- Book Copies Table
              CREATE TABLE IF NOT EXISTS BookCopy (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  book_id UUID NOT NULL,
                  copy_number INT NOT NULL,
                  is_available BOOLEAN DEFAULT TRUE,
                  FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
              );
          
              -- Checkout Entry Table
              CREATE TABLE IF NOT EXISTS CheckoutEntry (
                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                  book_copy_id UUID NOT NULL,
                  member_id UUID NOT NULL,
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

