package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Singleton;

@Module
public class RxSchedulersModule {


    @Provides
    @Singleton
    static Scheduler provideIoScheduler() {
        return Schedulers.io(); // Background thread for I/O operations
    }
}
