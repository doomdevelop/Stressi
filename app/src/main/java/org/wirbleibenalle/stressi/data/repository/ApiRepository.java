package org.wirbleibenalle.stressi.data.repository;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.data.remote.ServiceGenerator;
import org.wirbleibenalle.stressi.data.remote.service.EventService;
import org.wirbleibenalle.stressi.data.transformer.Transformer;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

import static org.wirbleibenalle.stressi.util.Constants.BASE_URL;

/**
 * Created by and on 26.10.16.
 */
@Singleton
public class ApiRepository {
    private final EventService eventService;
    private final Transformer<Events,List<EventItem>> transformer;

    @Inject
    public ApiRepository(ServiceGenerator serviceGenerator,Transformer<Events, List<EventItem>> transformer ) {
        this.eventService = serviceGenerator.createService(EventService.class);
        this.transformer = transformer;
    }

    public Observable<List<EventItem>> getEvents(LocalDate localDate) {
        return eventService.getEvents(localDate.toString()).map(generateTransformFunction());
    }

    private Func1<Events, List<EventItem>> generateTransformFunction() {
        Func1<Events, List<EventItem>> transformFunction = events -> transformer.transform(events);
        return transformFunction;
    }
}
