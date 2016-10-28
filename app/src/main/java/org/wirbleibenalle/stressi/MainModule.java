package org.wirbleibenalle.stressi;

import org.wirbleibenalle.stressi.data.repository.LocalRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by and on 26.10.16.
 */
@Module
public class MainModule {

    @Provides
    @Singleton
    public LocalRepository provideLocalRepository() {
        return new LocalRepository();
    }

}
