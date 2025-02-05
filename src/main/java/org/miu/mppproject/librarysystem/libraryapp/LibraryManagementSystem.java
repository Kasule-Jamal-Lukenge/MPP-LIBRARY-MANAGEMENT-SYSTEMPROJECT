package org.miu.mppproject.librarysystem.libraryapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.core.di.AppComponent;
import org.miu.mppproject.librarysystem.libraryapp.core.di.DaggerAppComponent;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.NavigationController;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.ScreenManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

public class LibraryManagementSystem extends Application {


    @Inject
    @Named("Credential")
    DataSource credentialDataSource;

    @Inject
    @Named("Lib")
    DataSource libDataSource;


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
        connectToDb();

    }


    private void connectToDb() {
        credentialDataSource.initializeDatabase();
        libDataSource.initializeDatabase();
    }


    public static void main(String[] args) {

        AppComponent appComponent = DaggerAppComponent.create();
        LibraryManagementSystem libraryManagementSystem = new LibraryManagementSystem();
        appComponent.inject(libraryManagementSystem);
        launch(args);
    }

}
