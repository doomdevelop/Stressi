package org.wirbleibenalle.stressi.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import org.wirbleibenalle.stressi.data.cache.CacheController;
import org.wirbleibenalle.stressi.data.cache.CacheInterceptor;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.model.CacheEvent;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.data.remote.ServiceGenerator;
import org.wirbleibenalle.stressi.data.remote.handler.NetworkConnectionHandler;
import org.wirbleibenalle.stressi.data.repository.LocalRepository;
import org.wirbleibenalle.stressi.data.transformer.EventTransformer;
import org.wirbleibenalle.stressi.data.transformer.Transformer;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static org.wirbleibenalle.stressi.util.Constants.BASE_URL;

@Module(includes = ViewModelModule.class)
public class MainModule {
    private static final String SHARE_PREF_NAME = "stressfaktor_pref";
    //timeout in min
    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 2;
    private static final int WRITE_TIMEOUT = 1;

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(SHARE_PREF_NAME, 0);
    }

    @Provides
    @Singleton
    public LocalRepository provideLocalRepository() {
        return new LocalRepository();
    }

    @Provides
    @Singleton
    public Transformer<Events, List<EventItem>> provideTransformer() {
        return new EventTransformer();
    }

    @Provides
    @Singleton
    public CacheController<CacheEvent> provideCacheController(SharedPreferences sharedPreferences) {
        return new EventCacheController(sharedPreferences);
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager(Application application) {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    public Builder provideBuilder() {
        return new OkHttpClient.Builder();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Builder builder, CacheInterceptor cacheInterceptor) {
        addCacheInterceptor(builder, cacheInterceptor);
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MINUTES);
        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JspoonConverterFactory.create()) // Simple XML converter
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava adapter
            .build();
    }

    @Provides
    @Singleton
    public CacheInterceptor provideCacheInterceptor(CacheController<CacheEvent> cacheController) {
        return new CacheInterceptor(cacheController);
    }

    @Provides
    @Singleton
    public ServiceGenerator provideServiceGenerator(Retrofit retrofit) {
        return new ServiceGenerator(retrofit);
    }

    @Provides
    @Singleton
    public NetworkConnectionHandler provideNetworkConnectionHandler(ConnectivityManager connectivityManager) {
        return new NetworkConnectionHandler(connectivityManager);
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

//    private void addLoggingInterceptor(final OkHttpClient.Builder builder) {
//
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(httpLoggingInterceptor);
//    }

    private void addCacheInterceptor(final OkHttpClient.Builder builder, CacheInterceptor cacheInterceptor) {
        if (!(cacheInterceptor instanceof Interceptor)) {
            throw new RuntimeException("cacheController must implement Interceptor");
        }
        builder.addInterceptor(cacheInterceptor);
    }
}