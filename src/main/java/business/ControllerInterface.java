package business;

import java.util.HashMap;
import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public HashMap<String, LibraryMember> allMembers();
	public void addMember(String memberId,String firstName,
						  String lastName, String telephone,
						  String street, String city,
						  String state, String zip) ;
	public void addBookCopy(String isbn, int quantity);
	public List<String> allBookIds();
	public HashMap<String,Book> allBooks();
	
}
