package dataaccess.dao;

import business.Book;
import business.CheckoutRecord;
import business.LibraryMember;
import dataaccess.DataAccessFacade;
import dataaccess.storage.DatabaseConnection;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CheckoutRecordDAO implements BaseDAO<CheckoutRecord, String> {
    @Override
    public void save(CheckoutRecord record) {
//        String query = "INSERT INTO checkout_records (member_id, book_isbn, checkout_date, due_date) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setString(1, record.getMember().getMemberId());
//            pstmt.setString(2, record.getBookCopy().getBook().getIsbn());
//            pstmt.setDate(3, Date.valueOf(record.getCheckoutDate()));
//            pstmt.setDate(4, Date.valueOf(record.getDueDate()));
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Optional<CheckoutRecord> findById(String s) {
        return Optional.empty();
    }

    @Override
    public List<CheckoutRecord> findAll() {
//        HashMap<String, CheckoutRecord> records = new HashMap<>();
//        String query = "SELECT * FROM checkout_records";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            while (rs.next()) {
//                LibraryMember member = readMemberMap().get(rs.getString("member_id"));
//                Book book = readBooksMap().get(rs.getString("book_isbn"));
//                CheckoutRecord record = new CheckoutRecord(member, book.getCopy(1), rs.getDate("checkout_date").toLocalDate(), 14);
//                records.put(member.getMemberId() + "_" + book.getIsbn(), record);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return records.values();
        return new ArrayList<>();
    }

    @Override
    public void update(CheckoutRecord entity) {

    }

    @Override
    public void delete(String s) {

    }
}
