package org.wirbleibenalle.stressi.data.cache;

import android.content.SharedPreferences;


public abstract class CacheController<T> {
    protected final SharedPreferences sharedPreferences;

    CacheController(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public abstract T getLastCache(String date);

    public abstract void cache(T t);

    public abstract void setOnPullToRefresh(String date, boolean value);

    public abstract boolean isOnPullToRefresh(String date);

    public abstract boolean canUseCache();
}
