package org.wirbleibenalle.stressi.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.wirbleibenalle.stressi.StressiApplication;
import org.wirbleibenalle.stressi.ui.base.BaseActivity;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

public class AppInjector {
    private AppInjector(){};
//    public static MainComponent init(StressiApplication stressiApplication) {
//        MainComponent mainComponent = DaggerMainComponent.builder().application
//            (stressiApplication).build().inject(stressiApplication).
//        stressiApplication
//            .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
//                @Override
//                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                    handleActivity(activity);
//                }
//
//                @Override
//                public void onActivityStarted(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivityResumed(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivityPaused(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivityStopped(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//                }
//
//                @Override
//                public void onActivityDestroyed(Activity activity) {
//
//                }
//            });
//        return mainComponent;
//    }

    private static void handleActivity(Activity activity) {
        if (activity instanceof BaseActivity) {
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                .registerFragmentLifecycleCallbacks(
                    new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentCreated(FragmentManager fm, Fragment f,
                                                      Bundle savedInstanceState) {
                            if (f instanceof Injectable) {
                                AndroidSupportInjection.inject(f);
                            }
                        }
                    }, true);
        }
    }
}
