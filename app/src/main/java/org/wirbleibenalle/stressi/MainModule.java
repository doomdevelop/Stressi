package org.wirbleibenalle.stressi;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import org.wirbleibenalle.stressi.data.cache.CacheController;
import org.wirbleibenalle.stressi.data.cache.CacheInterceptor;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.model.CacheEvent;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.data.remote.ErrorHandlerInterceptor;
import org.wirbleibenalle.stressi.data.remote.ServiceGenerator;
import org.wirbleibenalle.stressi.data.repository.LocalRepository;
import org.wirbleibenalle.stressi.data.transformer.EventTransformer;
import org.wirbleibenalle.stressi.data.transformer.Transformer;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static org.wirbleibenalle.stressi.util.Constants.BASE_URL;

/**
 * Created by and on 26.10.16.
 */
@Module
public class MainModule {
    private final Context context;
    private static final String SHARE_PREF_NAME = "stressfaktor_pref";

    MainModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.context;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARE_PREF_NAME,0);
    }

    @Provides
    @Singleton
    public LocalRepository provideLocalRepository() {
        return new LocalRepository();
    }

    @Provides
    @Singleton
    public Transformer<Events, List<EventItem>> provideTransformer() {
        Transformer<Events, List<EventItem>> transformer = new EventTransformer();
        return  transformer;
    }

    @Provides
    @Singleton
    public CacheController<CacheEvent> provideCacheController(SharedPreferences sharedPreferences){
        return new EventCacheController(sharedPreferences);
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr;

    }

    @Provides
    @Singleton
    public Builder provideBuilder(){
        return new OkHttpClient.Builder();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Builder builder, CacheController<CacheEvent> cacheController, ConnectivityManager connectivityManager){
//        addLoggingInterceptor(builder);
        addErrorHandlerInterceptor(builder,connectivityManager);
        addCacheInterceptor(builder,cacheController);
        OkHttpClient client = builder.build();
        return client;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Context context, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(JspoonConverterFactory.create()) // Simple XML converter
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
                .build();
    }

    @Provides
    @Singleton
    public ServiceGenerator provideServiceGenerator(Retrofit retrofit){
        return new ServiceGenerator(retrofit);
    }

    private void addLoggingInterceptor(final OkHttpClient.Builder builder) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
    }

    private void addCacheInterceptor(final OkHttpClient.Builder builder,CacheController cacheController){
        CacheInterceptor cacheInterceptor = new CacheInterceptor(cacheController);
        builder.addInterceptor(cacheInterceptor);
    }

    private void addErrorHandlerInterceptor(final OkHttpClient.Builder builder, ConnectivityManager connectivityManager){
        builder.addInterceptor(createErrorHandlerInterceptor(connectivityManager));
    }

    private ErrorHandlerInterceptor createErrorHandlerInterceptor(ConnectivityManager connectivityManager){
        return new ErrorHandlerInterceptor(connectivityManager);
    }
}