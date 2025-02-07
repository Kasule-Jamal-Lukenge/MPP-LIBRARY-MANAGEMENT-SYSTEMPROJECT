package business;

import java.io.Serializable;
import java.time.LocalDate;

final public class CheckoutRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private LibraryMember member;
    private BookCopy bookCopy;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private boolean isReturned;

    public CheckoutRecord(LibraryMember member, BookCopy bookCopy, LocalDate checkoutDate, int checkoutLength) {
        this.member = member;
        this.bookCopy = bookCopy;
        this.checkoutDate = checkoutDate;
        this.dueDate = checkoutDate.plusDays(checkoutLength);
        this.isReturned = false;
    }

    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(dueDate);
    }

    public void returnBook() {
        isReturned = true;
        bookCopy.changeAvailability();
    }

    public LibraryMember getMember() {
        return member;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}

