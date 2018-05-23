package org.wirbleibenalle.stressi.di;

import android.app.Application;

import org.wirbleibenalle.stressi.StressiApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, MainActivityModule.class, MainModule.class})
public interface MainComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        MainComponent build();
    }

    void inject(StressiApplication stressiApplication);
}
