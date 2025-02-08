package dataaccess.dao;

import business.Address;
import business.LibraryMember;
import dataaccess.storage.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LibraryMemberDAO implements BaseDAO<LibraryMember, String> {

    @Override
    public void save(LibraryMember member) {
        // First, insert the address into the address table
        String addressQuery = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?)";
        String memberQuery = "INSERT INTO member (memberId, firstName, lastName, telephone, address_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Insert the address
            try (PreparedStatement addressStmt = conn.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS)) {
                addressStmt.setString(1, member.getAddress().getStreet());
                addressStmt.setString(2, member.getAddress().getCity());
                addressStmt.setString(3, member.getAddress().getState());
                addressStmt.setString(4, member.getAddress().getZip());
                addressStmt.executeUpdate();

                // Get the generated address id
                try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int addressId = generatedKeys.getInt(1);  // Get the address id from the result

                        // Insert the member with the address id
                        try (PreparedStatement memberStmt = conn.prepareStatement(memberQuery)) {
                            memberStmt.setString(1, member.getMemberId());
                            memberStmt.setString(2, member.getFirstName());
                            memberStmt.setString(3, member.getLastName());
                            memberStmt.setString(4, member.getTelephone());
                            memberStmt.setInt(5, addressId); // Set the address id in the member table
                            memberStmt.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Creating address failed, no ID obtained.");
                    }
                }

                // Commit the transaction
                conn.commit();

            } catch (SQLException e) {
                // Rollback if any exception occurs
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, LibraryMember> getAllMembers() {
        HashMap<String, LibraryMember> members = new HashMap<>();
        String query = "SELECT m.memberId, m.firstName, m.lastName, m.telephone, a.street, a.city, a.state, a.zip " +
                "FROM member m " +
                "JOIN address a ON m.address_id = a.id";  // Join member table with address table
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Create Address object
                Address address = new Address(rs.getString("street"), rs.getString("city"),
                        rs.getString("state"), rs.getString("zip"));
                // Create LibraryMember object with Address
                LibraryMember member = new LibraryMember(rs.getString("memberId"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("telephone"),
                        address);
                // Add member to the map
                members.put(member.getMemberId(), member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // Load Members into Database
    public void loadMembers(List<LibraryMember> memberList) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String addressSql = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?) RETURNING id";
            String memberSql = "INSERT INTO member (memberId, firstName, lastName, telephone, address_id) VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (memberId) DO UPDATE SET firstName = EXCLUDED.firstName, lastName = EXCLUDED.lastName, " +
                    "telephone = EXCLUDED.telephone, address_id = EXCLUDED.address_id";

            try (PreparedStatement addressStmt = conn.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement memberStmt = conn.prepareStatement(memberSql)) {

                // Loop over each member in the list
                for (LibraryMember member : memberList) {
                    // Insert address and get the generated address_id
                    Address address = member.getAddress(); // Assuming LibraryMember has a getAddress() method
                    addressStmt.setString(1, address.getStreet());
                    addressStmt.setString(2, address.getCity());
                    addressStmt.setString(3, address.getState());
                    addressStmt.setString(4, address.getZip());
                    addressStmt.executeUpdate();

                    // Execute and retrieve the address_id
                    ResultSet addressRs = addressStmt.getGeneratedKeys();
                    int addressId = -1;
                    if (addressRs.next()) {
                        addressId = addressRs.getInt("id");
                    }

                    // Insert or update the member
                    memberStmt.setString(1, member.getMemberId());
                    memberStmt.setString(2, member.getFirstName());
                    memberStmt.setString(3, member.getLastName());
                    memberStmt.setString(4, member.getTelephone());
                    memberStmt.setInt(5, addressId);
                    memberStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<LibraryMember> findById(String memberId) {
        return Optional.empty(); // Implementation needed
    }

    @Override
    public List<LibraryMember> findAll() {
        return new ArrayList<>(); // Implementation needed
    }

    @Override
    public void update(LibraryMember member) {
        // Implementation needed
    }

    @Override
    public void delete(String memberId) {
        // Implementation needed
    }
}

