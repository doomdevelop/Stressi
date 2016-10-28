package org.wirbleibenalle.stressi.data.remote;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by and on 26.10.16.
 */

public class ServiceGenerator {
    private OkHttpClient.Builder okHttpBuilder;
    private Retrofit retrofit;

    @Inject
    public ServiceGenerator() {
        this.okHttpBuilder = new OkHttpClient.Builder();
    }

    public <S> S createService(Class<S> serviceClass, String baseUrl) {
        OkHttpClient client = okHttpBuilder.build();
        retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
            .addConverterFactory(SimpleXmlConverterFactory.create()) // Simple XML converter
            .build();
        return retrofit.create(serviceClass);
    }
}
