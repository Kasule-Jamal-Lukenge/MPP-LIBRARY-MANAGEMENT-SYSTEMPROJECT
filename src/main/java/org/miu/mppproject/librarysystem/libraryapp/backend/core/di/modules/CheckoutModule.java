package org.miu.mppproject.librarysystem.libraryapp.backend.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.CheckoutDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories.CheckoutEntryRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.ICheckoutEntryRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.ICheckoutEntryService;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl.CheckoutEntryServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

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
