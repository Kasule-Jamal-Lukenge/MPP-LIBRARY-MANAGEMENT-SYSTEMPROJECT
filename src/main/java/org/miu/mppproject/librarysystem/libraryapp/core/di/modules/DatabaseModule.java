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
import java.util.logging.Logger;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    @Named("Lib")
    static DataSource provideLibraryDataSource() {
        try {

            return new LibraryDataSource(DriverManager.getConnection("jdbc:postgresql://localhost:5432/librarydb", "Benklins", "Benklins@123"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Provides
    @Singleton
    @Named("Credential")
    static DataSource provideCredentialDataSource() {
        try {
            return new CredentialDataSource(DriverManager.getConnection("jdbc:postgresql://localhost:5432/credentialdb", "Benklins", "Benklins@123"));
        } catch (SQLException e) {
            Logger.getLogger(String.valueOf(DatabaseModule.class)).info(e.getMessage());
        }

        return null;
    }


}
