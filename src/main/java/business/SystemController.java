package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;


public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	
	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if(!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if(!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();
		
	}
	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}

	@Override
	public HashMap<String, LibraryMember> allMembers() {
		DataAccess da = new DataAccessFacade();
		return da.readMemberMap();
	}
	@Override
	public void addMember(String memberId,String firstName,
						  String lastName, String telephone,
						  String street, String city,
						  String state, String zip) {
		Address address = new Address(street, city, state, zip);
		LibraryMember member = new LibraryMember(memberId, firstName, lastName, telephone, address);
		DataAccess da = new DataAccessFacade();
		da.saveNewMember(member);
	}

	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}

	@Override
	public HashMap<String, Book> allBooks() {
		DataAccess da = new DataAccessFacade();
		return da.readBooksMap();
	}
	@Override
	public void addBookCopy(String isbn, int quantity) {
		DataAccess da = new DataAccessFacade();
        Book book = da.readBooksMap().get(isbn);
        if(book == null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found.");
        }
		BookCopy copy = new BookCopy(book, quantity+1, true);
		da.addBookCopy(copy);
	}
	@Override
	public void saveBook(Book newBook) {
       DataAccess da = new DataAccessFacade();
	   da.saveBook(newBook);
	}
//	public void checkoutBook(String memberId, String isbn) throws LibrarySystemException {
//		DataAccess da = new DataAccessFacade();
//
//		// Get the member and book
//		LibraryMember member = da.readMemberMap().get(memberId);
//		Book book = da.readBooksMap().get(isbn);
//
//		if (member == null) {
//			throw new LibrarySystemException("Member ID not found: " + memberId);
//		}
//
//		if (book == null) {
//			throw new LibrarySystemException("Book with ISBN " + isbn + " not found.");
//		}
//
//		// Check if book is available
//		BookCopy availableCopy = book.getNextAvailableCopy();
//		if (availableCopy == null) {
//			throw new LibrarySystemException("No available copies for ISBN: " + isbn);
//		}
//
//		// Create checkout record
//		CheckoutRecord record = new CheckoutRecord(member, availableCopy, LocalDate.now(), book.getMaxCheckoutLength());
//
//		// Mark book as checked out
//		availableCopy.changeAvailability();
//
//		// Save updated records
//		da.saveCheckoutRecord(record);
//	}
//	public List<String> getOverdueMembers() {
//		DataAccess da = new DataAccessFacade();
//		List<String> overdueMembers = new ArrayList<>();
//
//		Collection<CheckoutRecord> records = da.readCheckoutRecords();
//		for (CheckoutRecord record : records) {
//			if (record.isOverdue()) {
//				overdueMembers.add(record.getMember().getMemberId());
//			}
//		}
//
//		return overdueMembers;
//	}



}
