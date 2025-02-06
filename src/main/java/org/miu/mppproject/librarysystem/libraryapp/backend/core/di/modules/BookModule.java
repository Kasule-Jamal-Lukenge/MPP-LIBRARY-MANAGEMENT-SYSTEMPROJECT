package org.miu.mppproject.librarysystem.libraryapp.backend.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.BookDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories.BookRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IBookRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.IBookService;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl.BookServiceImpl;

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
