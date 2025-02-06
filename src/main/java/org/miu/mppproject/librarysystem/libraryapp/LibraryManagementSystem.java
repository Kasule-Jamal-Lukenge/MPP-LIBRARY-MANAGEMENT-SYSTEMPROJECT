package org.miu.mppproject.librarysystem.libraryapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.CredentialDataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.LibraryDataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.di.AppComponent;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.di.DaggerAppComponent;
import org.miu.mppproject.librarysystem.libraryapp.frontend.features.ShowScreenEvent;
import org.miu.mppproject.librarysystem.libraryapp.frontend.features.SplashScreen;
import org.miu.mppproject.librarysystem.libraryapp.frontend.navigation.NavigationController;

import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryManagementSystem extends Application {


//    @Inject
//    @Named("Credential")
//    DataSource credentialDataSource;
//
//    @Inject
//    @Named("Lib")
//    DataSource libDataSource;

    private NavigationController navigationController;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the NavigationController with the primary stage
        navigationController = new NavigationController(primaryStage);
        navigationController.initialize();

        // Create the splash screen and show it
        SplashScreen splashScreen = new SplashScreen(navigationController);

        // Use the navigation controller to display the splash screen
        navigationController.publishEvent(new ShowScreenEvent(splashScreen));
    }




    @Override
    public void init() throws Exception {
        super.init();
        initDb();

    }


    private void initDb() throws SQLException {

        DataSource credentialDataSource = new CredentialDataSource(DriverManager.getConnection("jdbc:postgresql://localhost:5432/credentialdb", "postgres", "Benklins@123"));
        DataSource libraryDataSource = new LibraryDataSource(DriverManager.getConnection("jdbc:postgresql://localhost:5432/librarydb", "postgres", "Benklins@123"));
        credentialDataSource.initializeDatabase();
        libraryDataSource.initializeDatabase();
    }


    public static void main(String[] args) {

        AppComponent appComponent = DaggerAppComponent.create();
        LibraryManagementSystem libraryManagementSystem = new LibraryManagementSystem();
        appComponent.inject(libraryManagementSystem);
        launch(args);
    }

}
