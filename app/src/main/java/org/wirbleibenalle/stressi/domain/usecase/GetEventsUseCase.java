package org.wirbleibenalle.stressi.domain.usecase;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.repository.DataRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by and on 26.10.16.
 */

public class GetEventsUseCase extends UseCase {
    private DataRepository dataRepository;
    private final LocalDate localDate;
    private final Integer day;

    @Inject
    public GetEventsUseCase(DataRepository dataRepository, LocalDate localDate, Integer day) {
        this.dataRepository = dataRepository;
        this.localDate = localDate;
        this.day = day;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return dataRepository.getEvents(localDate, day);
    }
}
