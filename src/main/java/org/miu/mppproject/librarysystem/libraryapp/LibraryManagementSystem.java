package org.miu.mppproject.librarysystem.libraryapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.CredentialDataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.LibraryDataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.di.AppComponent;
import org.miu.mppproject.librarysystem.libraryapp.core.di.DaggerAppComponent;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.NavigationController;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.ScreenManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class LibraryManagementSystem extends Application {


//    @Inject
//    @Named("Credential")
//    DataSource credentialDataSource;
//
//    @Inject
//    @Named("Lib")
//    DataSource libDataSource;


    @Override
    public void start(Stage primaryStage) {


        StackPane root = new StackPane();
        ScreenManager screenManager = new ScreenManager(root);
        new NavigationController(screenManager, root);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets()
                .add(Objects.requireNonNull(getClass()
                        .getResource("/styles.css")).toExternalForm()); // Add the CSS file
        primaryStage.setScene(scene);
        primaryStage.show();
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
