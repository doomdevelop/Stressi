package org.wirbleibenalle.stressi.data.cache;

import android.content.SharedPreferences;


public abstract class CacheController<T> {
    protected final SharedPreferences sharedPreferences;

    CacheController(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public abstract T getLastCache(String date);

    public abstract void cache(T t);

    public abstract long getLastCacheTime(String date);

    public abstract void putLastCacheTime(String date, long timestamp);

    public abstract boolean canUseCache();
}
