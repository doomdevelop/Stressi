package org.wirbleibenalle.stressi.di;

import android.app.Application;

import org.wirbleibenalle.stressi.StressiApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder mainModule(MainModule mainModule);

        MainComponent build();
    }
    void inject(StressiApplication stressiApplication);
//    void inject(MainActivity activity);

}
