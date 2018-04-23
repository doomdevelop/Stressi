package org.wirbleibenalle.stressi.data.remote;

import android.content.Context;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by and on 26.10.16.
 */

public class ServiceGenerator {
    private Retrofit retrofit;

    @Inject
    public ServiceGenerator(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
