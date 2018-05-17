package org.wirbleibenalle.stressi.data.cache;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.wirbleibenalle.stressi.data.model.CacheEvent;

import javax.inject.Inject;

public class EventCacheController extends CacheController<CacheEvent> {
    final static long CACHE_MAX_AGE = 10*60*1000;//10min cache limit
    private final static String PULL_KEY = "pull";
    private final Object syncCash = new Object();
    private final Object syncPullToRefresh = new Object();

    @Inject
    public EventCacheController(SharedPreferences sharedPreferences){
        super(sharedPreferences);
    }

    @Override
    public CacheEvent getLastCache(String date) {
        synchronized (syncCash) {
            String serialised = sharedPreferences.getString(date, null);
            if (serialised != null) {
                Gson gson = new Gson();
                CacheEvent cacheEvent = gson.fromJson(serialised, CacheEvent.class);
                long currentTime = System.currentTimeMillis();
                if (currentTime - cacheEvent.timestamp > CACHE_MAX_AGE) {
                    sharedPreferences.edit().remove(date).apply();
                    return null;
                }
                return cacheEvent;
            }
            return null;
        }
    }

    @Override
    public void cache(CacheEvent cacheEvent) {
        Gson gson = new Gson();
        String serialised = gson.toJson(cacheEvent);
        synchronized (syncCash) {
            sharedPreferences.edit().putString(cacheEvent.date, serialised).apply();
        }
    }

    @Override
    public void setOnPullToRefresh(String date, boolean value) {
        synchronized (syncPullToRefresh) {
            sharedPreferences.edit().putBoolean(date + PULL_KEY, value).apply();
        }
    }

    @Override
    public boolean isOnPullToRefresh(String date) {
        if(date == null){
            return false;
        }
        synchronized (syncPullToRefresh) {
            return sharedPreferences.getBoolean(date + PULL_KEY, false);
        }
    }

    @Override
    public boolean canUseCache() {
        return true;
    }
}
