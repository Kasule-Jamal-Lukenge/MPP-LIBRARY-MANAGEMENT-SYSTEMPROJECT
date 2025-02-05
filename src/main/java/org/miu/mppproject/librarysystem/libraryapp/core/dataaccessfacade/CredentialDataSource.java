package org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CredentialDataSource implements DataSource {


    private final Connection connection;

    public CredentialDataSource(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    @Override
    public void initializeDatabase() {
        createTable();
    }

    private void createTable() {
        String sql = """
                
                -- Create User Credential table
                CREATE TABLE user_credential (
                id VARCHAR(50) PRIMARY KEY,
                username VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(60) NOT NULL,  -- Increased length for hashed passwords
                role VARCHAR(20) NOT NULL
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
