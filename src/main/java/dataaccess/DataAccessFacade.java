package dataaccess;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

import business.*;
import dataaccess.DataAccessFacade.StorageType;


public class DataAccessFacade implements DataAccess {

	enum StorageType {
		BOOKS, MEMBERS, USERS;
	}
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/library";
	private static final String USER = "postgres";
	private static final String PASSWORD = "12345";
	public static final String DATE_PATTERN = "MM/dd/yyyy";


	static {
		initializeDatabase();
	}
	// Establish Database Connection
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER, PASSWORD);
	}

	private static void initializeDatabase() {
		try (Connection conn = DataAccessFacade.getConnection();
			 Statement stmt = conn.createStatement()) {

			// Books Table
			stmt.execute("CREATE TABLE IF NOT EXISTS book ( \n" +
					"    isbn VARCHAR(20) PRIMARY KEY, \n" +
					"    title VARCHAR(255), \n" +
					"    max_checkout_length INT \n" +
					");");

			// Address Table
			stmt.execute("CREATE TABLE IF NOT EXISTS address ( \n" +
					"    id SERIAL PRIMARY KEY, \n" +
					"    street VARCHAR(255), \n" +
					"    city VARCHAR(255), \n" +
					"    state VARCHAR(255), \n" +
					"    zip VARCHAR(10) \n" +
					");");

			// Library Membership Table
			// Members Table
			stmt.execute("CREATE TABLE IF NOT EXISTS member ( \n" +
					"    memberId VARCHAR(10) NOT NULL PRIMARY KEY, \n" +
					"    firstName VARCHAR(255) NOT NULL, \n" +
					"    lastName VARCHAR(255) NOT NULL, \n" +
					"    telephone VARCHAR(20), \n" +
					"    address_id INT NOT NULL, \n" +
					"    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE \n" +
					");");

			// Users Table
			stmt.execute("CREATE TABLE IF NOT EXISTS users (\n" +
					"    id VARCHAR(10) PRIMARY KEY, \n" +
					"    password VARCHAR(255), \n" +
					"    auth_level VARCHAR(20) NOT NULL CHECK (auth_level IN ('LIBRARIAN', 'ADMIN', 'BOTH'))\n" +
					");");

			// Book Copies Table
			stmt.execute("CREATE TABLE IF NOT EXISTS book_copy ( \n" +
					"    id SERIAL PRIMARY KEY, \n" +
					"    book_isbn VARCHAR(20) NOT NULL, \n" +
					"    copy_num INT NOT NULL, \n" +
					"    is_available BOOLEAN DEFAULT TRUE, \n" +
					"    CONSTRAINT fk_book FOREIGN KEY (book_isbn) REFERENCES book(isbn) ON DELETE CASCADE \n" +
					");");

			// Authors Table
			stmt.execute("CREATE TABLE IF NOT EXISTS author ( \n" +
					"    authorId SERIAL PRIMARY KEY, \n" +
					"    firstName VARCHAR(255) NOT NULL, \n" +
					"    lastName VARCHAR(255) NOT NULL, \n" +
					"    telephone VARCHAR(20), \n" +
					"    address_id INT NOT NULL, \n" +
					"    bio VARCHAR(255),\n" +
					"    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE \n" +
					");");

			// Books_Authors Table
			stmt.execute("CREATE TABLE IF NOT EXISTS book_author (\n" +
					"    book_isbn VARCHAR(20) NOT NULL, \n" +
					"    author_id INT NOT NULL, \n" +
					"    CONSTRAINT fk_book_authors_book FOREIGN KEY (book_isbn) REFERENCES book(isbn) ON DELETE CASCADE, \n" +
					"    CONSTRAINT fk_book_authors_author FOREIGN KEY (author_id) REFERENCES author(authorId) ON DELETE CASCADE\n" +
					");");
			// Checkout Records Table
			stmt.execute("CREATE TABLE IF NOT EXISTS checkout_record ( \n" +
					"    id SERIAL PRIMARY KEY, \n" +
					"    member_id VARCHAR(10) NOT NULL, \n" +
					"    book_copy_id INT NOT NULL, \n" +
					"    checkout_date DATE NOT NULL, \n" +
					"    due_date DATE NOT NULL, \n" +
					"    is_returned BOOLEAN DEFAULT FALSE, \n" +
					"    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member(memberId) ON DELETE CASCADE, \n" +
					"    CONSTRAINT fk_book_copy FOREIGN KEY (book_copy_id) REFERENCES book_copy(id) ON DELETE CASCADE \n" +
					");");  // Added the missing closing parenthesis

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveNewMember(LibraryMember member) {
		// First, insert the address into the address table
		String addressQuery = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?)";
		String memberQuery = "INSERT INTO member (memberId, firstName, lastName, telephone, address_id) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = getConnection()) {
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

	@Override
	public void saveBook(Book newBook) {
		String bookQuery = "INSERT INTO book (isbn, title, max_checkout_length) VALUES (?, ?, ?);";
		String addressQuery = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?);";
		String authorQuery = "INSERT INTO author (firstName, lastName, telephone, bio, address_id) VALUES (?, ?, ?, ?, ?)";
		String bookAuthorQuery = "INSERT INTO book_author (book_isbn, author_id) VALUES (?, ?) ON CONFLICT DO NOTHING;";
		String bookCopyQuery = "INSERT INTO book_copy (book_isbn, copy_num, is_available) VALUES (?, ?, ?) ON CONFLICT DO NOTHING;";

		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);

			// Insert Book
			try (PreparedStatement bookStmt = conn.prepareStatement(bookQuery)) {
				bookStmt.setString(1, newBook.getIsbn());
				bookStmt.setString(2, newBook.getTitle());
				bookStmt.setInt(3, newBook.getMaxCheckoutLength());
				bookStmt.executeUpdate();
			}

			// Insert Authors
			for (Author author : newBook.getAuthors()) {
				int addressId = -1;
				int authorId = -1;

				// Insert Address
				try (PreparedStatement addressStmt = conn.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS)) {
					Address address = author.getAddress();
					addressStmt.setString(1, address.getStreet());
					addressStmt.setString(2, address.getCity());
					addressStmt.setString(3, address.getState());
					addressStmt.setString(4, address.getZip());
					addressStmt.executeUpdate();

					try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							addressId = generatedKeys.getInt(1);
						}
					}
				}

				// Insert Author
				if (addressId != -1) {
					try (PreparedStatement authorStmt = conn.prepareStatement(authorQuery, Statement.RETURN_GENERATED_KEYS)) {
						authorStmt.setString(1, author.getFirstName());
						authorStmt.setString(2, author.getLastName());
						authorStmt.setString(3, author.getTelephone());
						authorStmt.setString(4, author.getBio());
						authorStmt.setInt(5, addressId);
						authorStmt.executeUpdate();

						try (ResultSet generatedKeys = authorStmt.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								authorId = generatedKeys.getInt(1);
							}
						}
					}
				}

				// Link Book to Author
				if (authorId != -1) {
					try (PreparedStatement bookAuthorStmt = conn.prepareStatement(bookAuthorQuery)) {
						bookAuthorStmt.setString(1, newBook.getIsbn());
						bookAuthorStmt.setInt(2, authorId);
						bookAuthorStmt.executeUpdate();
					}
				}
			}

			// Insert Book Copies
			for (BookCopy copy : newBook.getCopies()) {
				try (PreparedStatement bookCopyStmt = conn.prepareStatement(bookCopyQuery)) {
					bookCopyStmt.setString(1, newBook.getIsbn());
					bookCopyStmt.setInt(2, copy.getCopyNum());
					bookCopyStmt.setBoolean(3, copy.isAvailable());
					bookCopyStmt.executeUpdate();
				}
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public HashMap<String, Book> readBooksMap() {
		HashMap<String, Book> books = new HashMap<>();
		String query = "SELECT b.isbn, b.title, b.max_checkout_length, " +
				"       a.authorId, a.firstName AS author_first_name, a.lastName AS author_last_name, a.bio " +
				"FROM book b " +
				"LEFT JOIN book_author ba ON b.isbn = ba.book_isbn " +
				"LEFT JOIN author a ON ba.author_id = a.authorId";

		String copiesQuery = "SELECT bc.copy_num, bc.is_available FROM book_copy bc WHERE bc.book_isbn = ?";

		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {

			HashMap<String, List<Author>> authorsByIsbn = new HashMap<>();

			while (rs.next()) {
				String isbn = rs.getString("isbn");
				String title = rs.getString("title");
				int maxCheckoutLength = rs.getInt("max_checkout_length");

				// Collect authors
				if (rs.getInt("authorId") != 0) {
					String authorFirstName = rs.getString("author_first_name");
					String authorLastName = rs.getString("author_last_name");
					String authorBio = rs.getString("bio");
					Author author = new Author(authorFirstName, authorLastName, null, null, authorBio);

					authorsByIsbn.putIfAbsent(isbn, new ArrayList<>());
					if (!authorsByIsbn.get(isbn).contains(author)) {
						authorsByIsbn.get(isbn).add(author);
					}
				}

				// Ensure the book exists in the map
				books.putIfAbsent(isbn, new Book(isbn, title, maxCheckoutLength, authorsByIsbn.getOrDefault(isbn, new ArrayList<>())));
			}

			// Now, fetch the book copies for each book
			for (Map.Entry<String, Book> entry : books.entrySet()) {
				String isbn = entry.getKey();
				Book book = entry.getValue();

				book.clearCopies(); // Ensure the list is empty before adding copies

				try (PreparedStatement pstmt = conn.prepareStatement(copiesQuery)) {
					pstmt.setString(1, isbn);
					try (ResultSet copyRs = pstmt.executeQuery()) {
						while (copyRs.next()) {
							int copyNum = copyRs.getInt("copy_num");
							boolean isAvailable = copyRs.getBoolean("is_available");
							BookCopy bookCopy = new BookCopy(book, copyNum, isAvailable);
							book.addCopy(bookCopy);
						}
					}
				}
			}



		} catch (SQLException e) {
			e.printStackTrace();
		}

		return books;
	}


	// Read all members from PostgreSQL
	public HashMap<String, LibraryMember> readMemberMap() {
		HashMap<String, LibraryMember> members = new HashMap<>();
		String query = "SELECT m.memberId, m.firstName, m.lastName, m.telephone, a.street, a.city, a.state, a.zip " +
				"FROM member m " +
				"JOIN address a ON m.address_id = a.id";  // Join member table with address table
		try (Connection conn = DataAccessFacade.getConnection();
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


	// Read all users from PostgreSQL
	public HashMap<String, User> readUserMap() {
		HashMap<String, User> users = new HashMap<>();
		String query = "SELECT * FROM users";
		try (Connection conn = DataAccessFacade.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				User user = new User(rs.getString("id"), rs.getString("password"), Auth.valueOf(rs.getString("auth_level")));
				users.put(user.getId(), user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	// addBookCopy
	public void addBookCopy(BookCopy copy) {
		String query = "INSERT INTO book_copy (book_isbn, copy_num, is_available) VALUES (?,?,?)";
		try (Connection conn = DataAccessFacade.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, copy.getBook().getIsbn());
			pstmt.setInt(2, copy.getCopyNum());
			pstmt.setBoolean(3, copy.isAvailable());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Update book copy status

	// Save a checkout record
//	public void saveCheckoutRecord(CheckoutRecord record) {
//		String query = "INSERT INTO checkout_records (member_id, book_isbn, checkout_date, due_date) VALUES (?, ?, ?, ?)";
//		try (Connection conn = DataAccessFacade.getConnection();
//			 PreparedStatement pstmt = conn.prepareStatement(query)) {
//			pstmt.setString(1, record.getMember().getMemberId());
//			pstmt.setString(2, record.getBookCopy().getBook().getIsbn());
//			pstmt.setDate(3, Date.valueOf(record.getCheckoutDate()));
//			pstmt.setDate(4, Date.valueOf(record.getDueDate()));
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	// Read all checkout records
//	public Collection<CheckoutRecord> readCheckoutRecords() {
//		HashMap<String, CheckoutRecord> records = new HashMap<>();
//		String query = "SELECT * FROM checkout_records";
//		try (Connection conn = DataAccessFacade.getConnection();
//			 Statement stmt = conn.createStatement();
//			 ResultSet rs = stmt.executeQuery(query)) {
//			while (rs.next()) {
//				LibraryMember member = readMemberMap().get(rs.getString("member_id"));
//				Book book = readBooksMap().get(rs.getString("book_isbn"));
//				CheckoutRecord record = new CheckoutRecord(member, book.getCopy(1), rs.getDate("checkout_date").toLocalDate(), 14);
//				records.put(member.getMemberId() + "_" + book.getIsbn(), record);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return records.values();
//	}

	// Load Books into Database
	static void loadBookMap(List<Book> bookList) {
		List<Book> bookListTest = bookList;
		try (Connection conn = getConnection()) {
			String addressSql = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?) " +
					"ON CONFLICT (id) DO UPDATE SET street = EXCLUDED.street, city = EXCLUDED.city, " +
					"state = EXCLUDED.state, zip = EXCLUDED.zip RETURNING id";

			String authorSql = "INSERT INTO author (firstName, lastName, telephone, address_id, bio) " +
					"VALUES (?, ?, ?, ?, ?) " +
					"ON CONFLICT (authorId) DO UPDATE SET firstName = EXCLUDED.firstName, " +
					"lastName = EXCLUDED.lastName, telephone = EXCLUDED.telephone, " +
					"address_id = EXCLUDED.address_id, bio = EXCLUDED.bio";

			String bookSql = "INSERT INTO book (isbn, title, max_checkout_length) VALUES (?, ?, ?) " +
					"ON CONFLICT (isbn) DO UPDATE SET title = EXCLUDED.title, max_checkout_length = EXCLUDED.max_checkout_length";

			String bookAuthorSql = "INSERT INTO book_author (book_isbn, author_id) VALUES (?, ?) " +
					"ON CONFLICT DO NOTHING";
			// Insert book copy SQL
			String bookCopySql = "INSERT INTO book_copy (book_isbn, copy_num, is_available) VALUES (?, ?, ?)";

			try (PreparedStatement addressStmt = conn.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS);
				 PreparedStatement authorStmt = conn.prepareStatement(authorSql, Statement.RETURN_GENERATED_KEYS);
				 PreparedStatement bookStmt = conn.prepareStatement(bookSql);
				 PreparedStatement bookAuthorStmt = conn.prepareStatement(bookAuthorSql);
				 PreparedStatement bookCopyStmt = conn.prepareStatement(bookCopySql)) {

				for (Book book : bookList) {
					Map<Author, Integer> authorIdMap = new HashMap<>(); // Store authorId for each author
					for (Author author : book.getAuthors()) {
						// Insert address first
						Address address = author.getAddress(); // Assuming Author has an Address object
						int addressId = -1;


						if (address != null) {
							addressStmt.setString(1, address.getStreet());
							addressStmt.setString(2, address.getCity());
							addressStmt.setString(3, address.getState());
							addressStmt.setString(4, address.getZip());
							addressStmt.executeUpdate();

							ResultSet rs = addressStmt.getGeneratedKeys();
							if (rs.next()) {
								addressId = rs.getInt(1);
							}
						}

						// Insert author
						authorStmt.setString(1, author.getFirstName());
						authorStmt.setString(2, author.getLastName());
						authorStmt.setString(3, author.getTelephone());
						authorStmt.setInt(4, addressId); // Use the generated address ID
						authorStmt.setString(5, author.getBio());
						authorStmt.executeUpdate();

						ResultSet rs2 = authorStmt.getGeneratedKeys();
						if (rs2.next()) {
							int authorId = rs2.getInt(1);
							authorIdMap.put(author, authorId); // Store correct authorId
						}
					}

					// Insert book
					bookStmt.setString(1, book.getIsbn());
					bookStmt.setString(2, book.getTitle());
					bookStmt.setInt(3, book.getMaxCheckoutLength());
					bookStmt.executeUpdate();

					// Insert into book_author table
					for (Author author : book.getAuthors()) {
						Integer authorId = authorIdMap.get(author);
						bookAuthorStmt.setString(1, book.getIsbn());
						bookAuthorStmt.setInt(2, authorId);
						bookAuthorStmt.executeUpdate();
					}
					// Insert copies into book_copy table
					int numCopies = book.getNumCopies(); // Get the number of copies from the book object
					for (int copyNum = 1; copyNum <= numCopies; copyNum++) {
						bookCopyStmt.setString(1, book.getIsbn()); // Set book ISBN
						bookCopyStmt.setInt(2, copyNum); // Set copy number
						bookCopyStmt.setBoolean(3, true); // Set the copy as available initially
						bookCopyStmt.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	// Load Users into Database
	static void loadUserMap(List<User> userList) {
		try (Connection conn = getConnection()) {
			String sql = "INSERT INTO users (id, password, auth_level) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				for (User user : userList) {
					stmt.setString(1, user.getId());
					stmt.setString(2, user.getPassword());
					stmt.setString(3, user.getAuthorization().name());
					stmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Load Members into Database
	static void loadMemberMap(List<LibraryMember> memberList) {
		try (Connection conn = getConnection()) {
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


	final static class Pair<S,T> implements Serializable{

		S first;
		T second;
		Pair(S s, T t) {
			first = s;
			second = t;
		}
		@Override
		public boolean equals(Object ob) {
			if(ob == null) return false;
			if(this == ob) return true;
			if(ob.getClass() != getClass()) return false;
			@SuppressWarnings("unchecked")
			Pair<S,T> p = (Pair<S,T>)ob;
			return p.first.equals(first) && p.second.equals(second);
		}

		@Override
		public int hashCode() {
			return first.hashCode() + 5 * second.hashCode();
		}
		@Override
		public String toString() {
			return "(" + first.toString() + ", " + second.toString() + ")";
		}
		private static final long serialVersionUID = 5399827794066637059L;
	}

}
