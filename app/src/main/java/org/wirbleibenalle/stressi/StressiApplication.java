package org.wirbleibenalle.stressi;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

import org.wirbleibenalle.stressi.ui.DaggerMainComponent;
import org.wirbleibenalle.stressi.ui.MainComponent;

/**
 * Created by and on 26.10.16.
 */

public class StressiApplication extends Application {
    private MainComponent mainComponent;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mainComponent = DaggerMainComponent.create();
        context = getApplicationContext();
        JodaTimeAndroid.init(this);
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
