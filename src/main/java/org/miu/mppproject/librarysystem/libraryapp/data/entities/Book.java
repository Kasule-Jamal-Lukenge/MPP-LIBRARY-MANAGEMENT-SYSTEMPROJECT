package org.miu.mppproject.librarysystem.libraryapp.data.entities;

import java.util.ArrayList;
import java.util.List;

public final class Book extends BaseEntity {

    private final String isbn;
    private final String title;
    private final List<Author> authors;
    private int maxCheckoutLength;
    private  List<BookCopy> bookCopies;


    public Book(String isbn, String title,
                List<Author> authors,
                List<BookCopy> bookCopies,
                int maxCheckoutLength) {
        super("BK-");
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.bookCopies = bookCopies;
        this.maxCheckoutLength = maxCheckoutLength;

    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }


    public int getMaxCheckoutLength() {
        return maxCheckoutLength;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    public void setMaxCheckoutLength(int maxCheckoutLength) {
        this.maxCheckoutLength = maxCheckoutLength;
    }

    public void setBookCopies(List<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
    }
}
