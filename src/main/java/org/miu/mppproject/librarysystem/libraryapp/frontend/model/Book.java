package org.miu.mppproject.librarysystem.libraryapp.frontend.model;

import java.util.List;

public class Book {

    private final String title;

    private final String isbn;

    private final List<Author> authors;

    private final List<BookCopy>  bookCopies;

    public Book(String title, String isbn, List<Author> authors, List<BookCopy> bookCopies) {
        this.title = title;
        this.isbn = isbn;
        this.authors = authors;
        this.bookCopies = bookCopies;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }
}
