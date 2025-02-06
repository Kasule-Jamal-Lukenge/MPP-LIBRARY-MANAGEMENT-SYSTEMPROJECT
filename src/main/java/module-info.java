module org.miu.mppproject.librarysystem.libraryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires javax.inject;
    requires dagger;
    requires com.google.errorprone.annotations;
    requires jbcrypt;
    requires io.reactivex.rxjava3;
    requires fontawesomefx;

    opens org.miu.mppproject.librarysystem.libraryapp to javafx.fxml;
    exports org.miu.mppproject.librarysystem.libraryapp;


}