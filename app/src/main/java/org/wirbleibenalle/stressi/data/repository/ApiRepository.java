package org.wirbleibenalle.stressi.data.repository;

import org.wirbleibenalle.stressi.data.mapper.EventMapper;
import org.wirbleibenalle.stressi.data.model.RSS;
import org.wirbleibenalle.stressi.data.remote.ServiceGenerator;
import org.wirbleibenalle.stressi.data.remote.service.EventService;
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
    private ServiceGenerator serviceGenerator;

    @Inject
    public ApiRepository(ServiceGenerator serviceGenerator) {
        this.serviceGenerator = serviceGenerator;
    }

    private Func1<RSS, List<EventItem>> transformFunction = rss -> new EventMapper().transform(rss);

    public Observable<List<EventItem>> getEvents(long day) {
        EventService eventService = serviceGenerator.createService(EventService.class, BASE_URL);
        return eventService.getEvents(BASE_URL).map(transformFunction);
    }
}
