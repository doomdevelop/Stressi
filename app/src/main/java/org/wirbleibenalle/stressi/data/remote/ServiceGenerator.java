package org.wirbleibenalle.stressi.data.remote;

import javax.inject.Inject;

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
