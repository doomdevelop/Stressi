package org.wirbleibenalle.stressi.data.repository;


import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by and on 26.10.16.
 */
@Singleton
public class DataRepository {
    private ApiRepository apiRepository;
    private LocalRepository localRepository;

    @Inject
    public DataRepository(ApiRepository apiRepository, LocalRepository localRepository) {
        this.apiRepository = apiRepository;
        this.localRepository = localRepository;
    }


    public Observable<List<EventItem>> getEvents(long day) {
        return apiRepository.getEvents(day);
    }
}
