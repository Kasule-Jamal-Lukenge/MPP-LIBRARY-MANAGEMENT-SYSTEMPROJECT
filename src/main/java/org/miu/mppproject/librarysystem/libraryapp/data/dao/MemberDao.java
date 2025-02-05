package org.miu.mppproject.librarysystem.libraryapp.data.dao;

import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Address;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.ContactInfo;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MemberDao {
    private final DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(MemberDao.class.getName());

    public MemberDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addLibraryMember(Member member) throws SQLException {
        String contactInfoSQL = """
                INSERT INTO contact_info (id, firstname, lastname, phone_number, street, city, state, zipCode)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String memberSQL = """
                INSERT INTO members (id, contact_info_id) VALUES (?, ?)
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try (
                    PreparedStatement contactStmt = connection.prepareStatement(contactInfoSQL);
                    PreparedStatement memberStmt = connection.prepareStatement(memberSQL)
            ) {
                // Insert Contact Info
                contactStmt.setString(1, member.getContactInfo().getId());
                contactStmt.setString(2, member.getContactInfo().getFirstName());
                contactStmt.setString(3, member.getContactInfo().getLastName());
                contactStmt.setString(4, member.getContactInfo().getPhoneNumber());
                contactStmt.setString(5, member.getContactInfo().getAddress().street());
                contactStmt.setString(6, member.getContactInfo().getAddress().city());
                contactStmt.setString(7, member.getContactInfo().getAddress().state());
                contactStmt.setString(8, member.getContactInfo().getAddress().zipCode());
                contactStmt.executeUpdate();

                // Insert Member
                memberStmt.setString(1, member.getMemberId());
                memberStmt.setString(2, member.getContactInfo().getId());
                memberStmt.executeUpdate();

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback in case of failure
                throw e;
            }
        }
    }

    public void editMember(Member member) throws SQLException {
        String sql = """
                UPDATE contact_info
                SET phone_number = ?, street = ?, city = ?, state = ?, zipCode = ?
                WHERE id = (SELECT contact_info_id FROM members WHERE id = ?)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);  // Start transaction

            stmt.setString(1, member.getContactInfo().getPhoneNumber());
            stmt.setString(2, member.getContactInfo().getAddress().street());
            stmt.setString(3, member.getContactInfo().getAddress().city());
            stmt.setString(4, member.getContactInfo().getAddress().state());
            stmt.setString(5, member.getContactInfo().getAddress().zipCode());
            stmt.setString(6, member.getMemberId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No member found with the given ID");
            }

            connection.commit();  // Commit transaction
        } catch (SQLException e) {
            throw new SQLException("Error editing member", e);
        }
    }

    public Optional<Member> findByMemberId(String memberId) throws SQLException {
        String sql = """
                SELECT m.id AS member_id, 
                       ci.id AS contact_id, ci.phone_number, ci.firstname, ci.lastname,
                       ci.street, ci.city, ci.state, ci.zipCode
                FROM members m
                JOIN contact_info ci ON m.contact_info_id = ci.id
                WHERE m.id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ContactInfo contactInfo = new ContactInfo(
                            rs.getString("phone_number"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            new Address(
                                    rs.getString("street"),
                                    rs.getString("city"),
                                    rs.getString("state"),
                                    rs.getString("zipCode")
                            )
                    );

                    Member member = new Member(contactInfo);
                    return Optional.of(member);
                }
            }
        }
        return Optional.empty();
    }

    public List<Member> getAllMembers() throws SQLException {
        String sql = """
                SELECT m.id AS member_id, 
                       ci.id AS contact_id, ci.phone_number, ci.firstname, ci.lastname,
                       ci.street, ci.city, ci.state, ci.zipCode
                FROM members m
                JOIN contact_info ci ON m.contact_info_id = ci.id
                """;

        List<Member> members = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ContactInfo contactInfo = new ContactInfo(
                        rs.getString("phone_number"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        new Address(
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getString("zipCode")
                        )
                );

                members.add(new Member(contactInfo));
            }
        }
        return members;
    }
}
