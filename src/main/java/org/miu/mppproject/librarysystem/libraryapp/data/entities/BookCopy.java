package org.miu.mppproject.librarysystem.libraryapp.data.entities;



public class BookCopy extends BaseEntity {

    private int copyNumber;
    private boolean isAvailable;
    private Book book;

    public BookCopy(boolean isAvailable, int copyNumber, Book book) {
        super("BC");
        this.book = book;
        this.isAvailable = isAvailable;
        this.copyNumber = copyNumber;
    }

    @Override
    public String getId() {
        return String.valueOf(copyNumber);
    }


    public void setBook(Book book) {
        this.book = book;
    }

    public int getCopyNumber() {
        return copyNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Book getBook() {
        return book;
    }
}
