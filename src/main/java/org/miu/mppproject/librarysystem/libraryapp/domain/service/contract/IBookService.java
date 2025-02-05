package org.miu.mppproject.librarysystem.libraryapp.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Book;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;

import java.util.List;
import java.util.Optional;

public interface IBookService {


    List<Book> getAllLibraryBooks();

    List<Book> getAllBookCopies(String isbn);

    void addBook(Book book);

    Optional<Member> checkBookCopyNumberAndIsWith(String copyNumber, String isbn);


}
