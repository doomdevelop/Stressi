package org.wirbleibenalle.stressi.ui;

import org.wirbleibenalle.stressi.di.MainModule;
import org.wirbleibenalle.stressi.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by and on 26.10.16.
 */
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);

}
