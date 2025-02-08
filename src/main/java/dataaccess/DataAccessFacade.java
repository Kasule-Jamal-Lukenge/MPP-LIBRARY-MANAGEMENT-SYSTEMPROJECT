package dataaccess;

import java.util.*;

import business.*;
import dataaccess.dao.*;


public class DataAccessFacade implements DataAccess {

	enum StorageType {
		BOOKS, MEMBERS, USERS;
	}

	// Establish Database Connection

	public void saveNewMember(LibraryMember member) {
		LibraryMemberDAO memberDAO = DAOFactory.getLibraryMemberDAO();
		memberDAO.save(member);
	}

	@Override
	public void saveBook(Book newBook) {
		BookDAO bookDAO = DAOFactory.getBookDAO();
		bookDAO.save(newBook);
	}

	public HashMap<String, Book> readBooksMap() {
		BookDAO bookDAO = DAOFactory.getBookDAO();
		return bookDAO.getAllBooks();
	}

	// Read all members from PostgreSQL
	public HashMap<String, LibraryMember> readMemberMap() {
		LibraryMemberDAO memberDAO = DAOFactory.getLibraryMemberDAO();
		return memberDAO.getAllMembers();
	}

	// Read all users from PostgreSQL
	public HashMap<String, User> readUserMap() {
		UserDAO userDAO = DAOFactory.getUserDAO();
        return userDAO.getAllUsers();
	}

	// addBookCopy
	public void addBookCopy(BookCopy copy) {
		BookDAO bookDAO = DAOFactory.getBookDAO();
        bookDAO.addBookCopy(copy);
	}

	// Save a checkout record
	public void saveCheckoutRecord(CheckoutRecord record) {
		CheckoutRecordDAO checkoutRecordDAO = DAOFactory.getCheckoutRecordDAO();
		checkoutRecordDAO.save(record);
	}

	// Read all checkout records
	public List<CheckoutRecord> readCheckoutRecords() {
		CheckoutRecordDAO checkoutRecordDAO = DAOFactory.getCheckoutRecordDAO();
		return checkoutRecordDAO.findAll();
	}

	// Load Books into Database
	static void loadBookMap(List<Book> bookList) {
		BookDAO bookDAO = DAOFactory.getBookDAO();
		bookDAO.loadBooks(bookList);
	}

	// Load Users into Database
	static void loadUserMap(List<User> userList) {
		UserDAO userDAO = DAOFactory.getUserDAO();
		userDAO.loadUsers(userList);
	}

	// Load Members into Database
	static void loadMemberMap(List<LibraryMember> memberList){
		LibraryMemberDAO memberDAO = DAOFactory.getLibraryMemberDAO();
        memberDAO.loadMembers(memberList);
	}

}
