package org.miu.mppproject.librarysystem.libraryapp.frontend.model;

public class CheckoutEntry {

    private final String bookTitle;

    private final String isbn;

    private final String bookCopyNumber;

    private final String dateBorrowed;

    private final String dueDate;

    private final double fineAmount;

    private final boolean status;

    private final boolean overDue;

    public String getIsbn() {
        return isbn;
    }

    public CheckoutEntry(String bookTitle, String isbn,
                         String bookCopyNumber,
                         String dateBorrowed,
                         String dueDate,
                         double fineAmount,
                         boolean status,
                         boolean overDue) {
        this.bookTitle = bookTitle;
        this.isbn = isbn;
        this.bookCopyNumber = bookCopyNumber;
        this.dateBorrowed = dateBorrowed;
        this.dueDate = dueDate;
        this.fineAmount = fineAmount;
        this.status = status;
        this.overDue = overDue;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookCopyNumber() {
        return bookCopyNumber;
    }

    public String getDateBorrowed() {
        return dateBorrowed;
    }

    public String getDueDate() {
        return dueDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isOverDue() {
        return overDue;
    }
}
