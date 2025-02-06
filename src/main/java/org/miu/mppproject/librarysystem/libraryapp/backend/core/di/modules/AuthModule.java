package org.miu.mppproject.librarysystem.libraryapp.backend.core.di.modules;

import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.security.AuthenticationServer;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.security.CredentialDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.AuthDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.UserDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IAuthRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories.AuthRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.IAuthService;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl.AuthServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

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
