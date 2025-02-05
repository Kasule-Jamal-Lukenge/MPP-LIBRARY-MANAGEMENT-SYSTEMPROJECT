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
                CREATE TABLE IF NOT EXISTS user_credential (
                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    username VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL, 
                    role VARCHAR(50) NOT NULL
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user_credential table", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
