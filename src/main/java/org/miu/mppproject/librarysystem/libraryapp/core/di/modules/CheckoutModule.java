package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.BookDao;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.CheckoutDao;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.CheckoutEntryRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.Data;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.ICheckoutEntryRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.ICheckoutEntryService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.CheckoutEntryServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;

@Module
public class CheckoutModule {


    @Provides
    @Singleton
    static ICheckoutEntryRepository provideCheckoutEntryRepository(CheckoutDao checkoutDao) {
        return new CheckoutEntryRepositoryImpl(checkoutDao);
    }

    @Provides
    @Singleton
    static ICheckoutEntryService provideCheckoutEntryService(ICheckoutEntryRepository checkoutEntryRepository) {

        return new CheckoutEntryServiceImpl(checkoutEntryRepository);
    }


    @Provides
    @Singleton
    static CheckoutDao provideCheckoutEntryDao(@Named("Lib") DataSource dataSource) {
        return new CheckoutDao(dataSource);
    }
}
