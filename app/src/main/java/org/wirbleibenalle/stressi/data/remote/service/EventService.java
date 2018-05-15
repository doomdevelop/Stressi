package org.wirbleibenalle.stressi.data.remote.service;

import org.wirbleibenalle.stressi.data.model.Events;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventService {
    @GET("termine.php")
    Observable<Events> getEvents(@Query("day") String day);
}
