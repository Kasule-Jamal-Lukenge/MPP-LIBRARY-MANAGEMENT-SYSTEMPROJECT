package org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Book;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.BookCopy;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Member;

import java.util.List;
import java.util.Optional;

public interface IBookService {


    List<Book> getAllLibraryBooks();

    List<BookCopy> getAllBookCopies(String isbn);

    void addBook(Book book);

    Optional<Member> checkBookCopyNumberAndIsWith(String copyNumber, String isbn);


}
