package org.wirbleibenalle.stressi.domain.usecase;

import org.wirbleibenalle.stressi.data.repository.DataRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by and on 26.10.16.
 */

public class GetEventsUseCase extends UseCase {
    private DataRepository dataRepository;
    private long day;

    @Inject
    public GetEventsUseCase(DataRepository dataRepository, long day) {
        this.dataRepository = dataRepository;
        this.day = day;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return dataRepository.getEvents(day);
    }
}
