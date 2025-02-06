package org.miu.mppproject.librarysystem.libraryapp.backend.core.di;

import dagger.Component;
import org.miu.mppproject.librarysystem.libraryapp.LibraryManagementSystem;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.di.modules.*;
//import org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth.AuthController;

import javax.inject.Singleton;


@Singleton
@Component(modules = {
        DatabaseModule.class,
//        RxSchedulersModule.class,
        AuthModule.class,
        MemberModule.class,
        BookModule.class,
        CheckoutModule.class
})
public interface AppComponent {
//    void inject(AuthController controller);


    void inject(LibraryManagementSystem libraryManagementSystem);


}
