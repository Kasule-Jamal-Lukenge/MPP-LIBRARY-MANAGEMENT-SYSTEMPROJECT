package dataaccess.dao;

public class DAOFactory {
    public static LibraryMemberDAO getLibraryMemberDAO() {
        return new LibraryMemberDAO();
    }

    public static BookDAO getBookDAO() {
        return new BookDAO();
    }
    public static UserDAO getUserDAO() {
        return new UserDAO();
    }
    public static CheckoutRecordDAO getCheckoutRecordDAO(){
        return new CheckoutRecordDAO();
    }
}

