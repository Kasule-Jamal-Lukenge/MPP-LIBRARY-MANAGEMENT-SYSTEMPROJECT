package org.miu.mppproject.librarysystem.libraryapp.backend.core.di.modules;

import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.UserDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories.UserManagementRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IUserManagementRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.IUserManagementService;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl.UserManagementManagementServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class UserManagementModule {


    @Provides
    @Singleton
    static UserDao provideUserManagementDao(@Named("Lib") DataSource dataSource) {
        return new UserDao(dataSource);
    }

    @Provides
    @Singleton
    static IUserManagementRepository provideUserManagementRepository(UserDao userDao) {
        return new UserManagementRepositoryImpl(userDao);
    }

    @Provides
    @Singleton
    static IUserManagementService provideUserManagementService(IUserManagementRepository repository) {
        return new UserManagementManagementServiceImpl(repository);
    }


}
