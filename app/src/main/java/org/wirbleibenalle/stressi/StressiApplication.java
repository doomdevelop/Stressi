package org.wirbleibenalle.stressi;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import org.wirbleibenalle.stressi.di.MainModule;
import org.wirbleibenalle.stressi.stressfaktor.BuildConfig;
import org.wirbleibenalle.stressi.ui.DaggerMainComponent;
import org.wirbleibenalle.stressi.ui.MainComponent;

import timber.log.Timber;
import timber.log.Timber.DebugTree;


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
        JodaTimeAndroid.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule(getApplicationContext())).build();
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
//            FakeCrashLibrary.log(priority, tag, message);
            switch (priority) {
                case Log.ERROR:
                    //TODO:add log to Crash report tool
                    break;
                case Log.WARN:
                    break;
            }
        }
    }
}
