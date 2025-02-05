package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.BookDao;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.BookRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IBookRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IBookService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.BookServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class BookModule {


    @Provides
    @Singleton
    static IBookRepository provideBookRepository(BookDao bookDao) {
        return new BookRepositoryImpl(bookDao);
    }

    @Provides
    @Singleton
    static IBookService provideBookService(IBookRepository bookRepository) {

        return new BookServiceImpl(bookRepository);
    }


    @Provides
    @Singleton
    static BookDao provideBookDao(@Named("Lib") DataSource dataSource) {
        return new BookDao(dataSource);
    }

}
