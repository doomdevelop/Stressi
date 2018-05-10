package org.wirbleibenalle.stressi.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import org.wirbleibenalle.stressi.ui.base.StressiViewModelFactory;
import org.wirbleibenalle.stressi.ui.main.MainPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainPresenter.class)
    abstract ViewModel bindMainPresenter(MainPresenter mainPresenter);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(StressiViewModelFactory factory);
}
