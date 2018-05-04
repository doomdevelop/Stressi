package org.wirbleibenalle.stressi;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import org.wirbleibenalle.stressi.ui.DaggerMainComponent;
import org.wirbleibenalle.stressi.ui.MainComponent;

/**
 * Created by and on 26.10.16.
 */

public class StressiApplication extends Application {
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule(getApplicationContext())).build();
        JodaTimeAndroid.init(this);
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
