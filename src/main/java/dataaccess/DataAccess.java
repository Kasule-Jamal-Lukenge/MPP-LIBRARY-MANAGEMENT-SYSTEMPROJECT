package dataaccess;

import java.util.HashMap;

import business.Book;
import business.BookCopy;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public void addBookCopy(BookCopy copy);
	public void saveNewMember(LibraryMember member);
	public void saveBook(Book newBook);
}
