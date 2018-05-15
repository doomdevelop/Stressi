package org.wirbleibenalle.stressi.domain.usecase;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.repository.DataRepository;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class GetEventsUseCase extends UseCase<List<EventItem>> {
    private final DataRepository dataRepository;
    private LocalDate localDate;

    @Inject
    public GetEventsUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    protected Observable<List<EventItem>> buildUseCaseObservable() {
        return dataRepository.getEvents(localDate);
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
