package org.miu.mppproject.librarysystem.libraryapp.domain.service.impl;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Book;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.BookCopy;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IBookRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IBookService;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements IBookService {
    private final IBookRepository bookRepository;

    public BookServiceImpl(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> getAllLibraryBooks() {
        return bookRepository.getAllLibraryBooks();
    }

    @Override
    public List<BookCopy> getAllBookCopies(String isbn) {
        return bookRepository.getAllBookCopies(isbn);
    }

    public void addBook(Book book) {
        bookRepository.addBook(book);
    }



    @Override
    public Optional<Member> checkBookCopyNumberAndIsWith(String copyNumber, String isbn) {
        return bookRepository.checkBookCopyNumberAndIsWith(copyNumber,isbn);
    }


}

