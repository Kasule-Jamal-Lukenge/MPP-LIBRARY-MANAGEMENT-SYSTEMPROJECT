package org.miu.mppproject.librarysystem.libraryapp.data.repositories;

import org.miu.mppproject.librarysystem.libraryapp.core.utlis.exception.BookException;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.BookDao;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Book;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.BookCopy;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IBookRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements IBookRepository {


    private final BookDao bookDao;

    public BookRepositoryImpl(BookDao bookDao) {

        this.bookDao = bookDao;

    }


    @Override
    public List<Book> getAllLibraryBooks() {
        try {
            return bookDao.getAllLibraryBooks();
        } catch (SQLException e) {
            throw new BookException("Something went wrong fetching books");
        }
    }

    @Override
    public List<BookCopy> getAllBookCopies(String isbn) {
        try {
            return bookDao.getAllBookCopies(isbn);
        } catch (SQLException e) {
            throw new BookException("unable to book copies");
        }
    }

    @Override
    public void addBook(Book book) {
        try {
            bookDao.addBook(book);
        } catch (SQLException e) {
            throw new BookException("Unable to add Book");
        }
    }


    @Override
    public Optional<Member> checkBookCopyNumberAndIsWith(String copyNumber, String isbn) {
        return Optional.empty();
    }


}
