package org.wirbleibenalle.stressi.domain.usecase;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.repository.DataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * Created by and on 26.10.16.
 */
public class GetEventsUseCase extends UseCase {
    private DataRepository dataRepository;
    private LocalDate localDate;
    private Integer position;


    @Inject
    public GetEventsUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return dataRepository.getEvents(localDate, position);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void unsubscribe() {
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
