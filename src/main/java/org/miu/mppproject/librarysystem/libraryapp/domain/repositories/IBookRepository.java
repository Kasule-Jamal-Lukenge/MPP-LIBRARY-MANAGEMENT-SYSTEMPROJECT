package org.miu.mppproject.librarysystem.libraryapp.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Book;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.BookCopy;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;

import java.util.List;
import java.util.Optional;

public interface IBookRepository {


    List<Book> getAllLibraryBooks();

    List<BookCopy> getAllBookCopies(String isbn);

    void addBook(Book book);


    Optional<Member> checkBookCopyNumberAndIsWith(String copyNumber, String isbn);
}

