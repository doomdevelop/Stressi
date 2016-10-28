package org.wirbleibenalle.stressi.data.remote.service;

import org.wirbleibenalle.stressi.data.model.RSS;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by and on 26.10.16.
 */

public interface EventService {
    @GET
    Observable<RSS> getEvents(@Url String feedUrl);
}
