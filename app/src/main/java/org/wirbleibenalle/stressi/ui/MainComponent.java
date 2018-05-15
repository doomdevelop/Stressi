package org.wirbleibenalle.stressi.ui;

import org.wirbleibenalle.stressi.di.MainModule;
import org.wirbleibenalle.stressi.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);

}
