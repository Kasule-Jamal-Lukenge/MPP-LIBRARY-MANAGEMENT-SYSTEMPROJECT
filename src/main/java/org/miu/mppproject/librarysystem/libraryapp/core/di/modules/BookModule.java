package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.checkerframework.checker.units.qual.C;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.security.AuthenticationServer;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.AuthDao;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.BookDao;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.UserDao;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.AuthRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.BookRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IAuthRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IBookRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IAuthService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IBookService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.AuthServiceImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.BookServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;

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
