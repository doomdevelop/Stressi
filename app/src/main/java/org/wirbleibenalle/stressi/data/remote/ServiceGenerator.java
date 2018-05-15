package org.wirbleibenalle.stressi.data.remote;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class ServiceGenerator {
    private final Retrofit retrofit;

    @Inject
    public ServiceGenerator(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
