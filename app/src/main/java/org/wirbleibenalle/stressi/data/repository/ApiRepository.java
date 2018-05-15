package org.wirbleibenalle.stressi.data.repository;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.data.remote.ServiceGenerator;
import org.wirbleibenalle.stressi.data.remote.service.EventService;
import org.wirbleibenalle.stressi.data.transformer.EventTransformer;
import org.wirbleibenalle.stressi.data.transformer.Transformer;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

@Singleton
public class ApiRepository {
    private final EventService eventService;
    private final Transformer<Events, List<EventItem>> transformer;

    @Inject
    public ApiRepository(ServiceGenerator serviceGenerator, Transformer<Events, List<EventItem>> transformer) {
        this.eventService = serviceGenerator.createService(EventService.class);
        this.transformer = transformer;
    }

    public Observable<List<EventItem>> getEvents(LocalDate localDate) {
        if (transformer instanceof EventTransformer) {
            ((EventTransformer) transformer).setLocalDate(localDate);
        }
        return eventService.getEvents(localDate.toString()).map(generateTransformFunction());
    }

    private Function<Events, List<EventItem>> generateTransformFunction() {
        return transformer::transform;
    }
}
