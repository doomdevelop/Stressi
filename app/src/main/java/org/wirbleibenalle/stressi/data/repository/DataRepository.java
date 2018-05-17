package org.wirbleibenalle.stressi.data.repository;


import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class DataRepository {
    private final ApiRepository apiRepository;
    private final LocalRepository localRepository;

    @Inject
    public DataRepository(ApiRepository apiRepository, LocalRepository localRepository) {
        this.apiRepository = apiRepository;
        this.localRepository = localRepository;
    }

    public Observable<List<EventItem>> getEvents(LocalDate localDate) {
        return apiRepository.getEvents(localDate);
    }
}
