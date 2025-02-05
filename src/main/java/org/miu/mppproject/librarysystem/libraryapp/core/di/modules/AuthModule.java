package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;

import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.security.AuthenticationServer;
import org.miu.mppproject.librarysystem.libraryapp.core.security.CredentialDao;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.AuthDao;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.UserDao;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IAuthRepository;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.AuthRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IAuthService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.AuthServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;

@Module
public abstract class AuthModule {


    @Provides
    @Singleton
    static IAuthRepository provideAuthRepository(UserDao userDao, AuthDao authDao) {
        return new AuthRepositoryImpl(authDao, userDao);
    }

    @Provides
    @Singleton
    static IAuthService provideAuthService(IAuthRepository repository) {
        return new AuthServiceImpl(repository);
    }


    @Provides
    @Singleton
    static AuthDao provideAuthDao(AuthenticationServer authenticationServer) {
        return new AuthDao(authenticationServer);
    }

    @Provides
    @Singleton
    static CredentialDao provideCredentialDao(@Named("Credential") DataSource dataSource) {
        return new CredentialDao(dataSource.getConnection());
    }

    @Provides
    @Singleton
    static AuthenticationServer provideAuthenticationServer() {
        return new AuthenticationServer();
    }


}
