package org.miu.mppproject.librarysystem.libraryapp.frontend.model;

public class BookCopy {


    private final String copyNumber;

    private  final String bookIsbn;

    private final boolean isAvailable;

    public BookCopy(String copyNumber, String bookIsbn, boolean isAvailable) {
        this.copyNumber = copyNumber;
        this.bookIsbn = bookIsbn;
        this.isAvailable = isAvailable;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
