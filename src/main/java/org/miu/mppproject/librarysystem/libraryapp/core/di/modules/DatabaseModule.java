package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;

import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.CredentialDataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.LibraryDataSource;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.DriverManager;
import java.sql.SQLException;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    @Named("Lib")
    static DataSource provideLibraryDataSource() {
        try {

            return new LibraryDataSource(DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraryDb", "", ""));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Provides
    @Singleton
    @Named("Credential")
    static DataSource provideCredentialDataSource() {
        try {
            return new CredentialDataSource(DriverManager.getConnection("jdbc:postgresql://localhost:5432/CredentialDb", "", ""));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
