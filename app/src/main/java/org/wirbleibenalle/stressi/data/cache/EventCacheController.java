package org.wirbleibenalle.stressi.data.cache;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.wirbleibenalle.stressi.data.model.CacheEvent;

import javax.inject.Inject;

public class EventCacheController extends CacheController<CacheEvent> {
    final static long CACHE_MAX_AGE = 10*60*1000;//10min cache limit

    @Inject
    public EventCacheController(SharedPreferences sharedPreferences){
        super(sharedPreferences);
    }

    @Override
    public CacheEvent getLastCache(String date) {
        String serialised = sharedPreferences.getString(date,null);
        if(serialised != null){
            Gson gson = new Gson();
            CacheEvent cacheEvent = gson.fromJson(serialised,CacheEvent.class);
            long currentTime = System.currentTimeMillis();
            if(currentTime-cacheEvent.timestamp >CACHE_MAX_AGE){
                sharedPreferences.edit().remove(date).commit();
                return null;
            }
            return cacheEvent;
        }
        return null;
    }

    @Override
    public void cache(CacheEvent cacheEvent) {
        Gson gson = new Gson();
        String serialised = gson.toJson(cacheEvent);
        sharedPreferences.edit().putString(cacheEvent.date,serialised).commit();
    }

    @Override
    public long getLastCacheTime(String date) {
        return 0;
    }

    @Override
    public void putLastCacheTime(String date, long timestamp) {

    }

    @Override
    public boolean canUseCache() {
        return true;
    }
}
