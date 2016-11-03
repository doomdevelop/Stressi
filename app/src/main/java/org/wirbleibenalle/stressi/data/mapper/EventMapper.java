package org.wirbleibenalle.stressi.data.mapper;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.EventRss;
import org.wirbleibenalle.stressi.data.model.RSS;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by and on 28.10.16.
 */

public class EventMapper {
    private final LocalDate localDate;
    private final Integer day;

    public EventMapper(LocalDate localDate, Integer day) {
        this.localDate = localDate;
        this.day = day;
    }

    public List<EventItem> transform(RSS rss) {
        List<EventRss> eventRssList = rss.getChannelRss().getFeedItems();
        List<EventItem> eventItemList = new ArrayList<>(eventRssList.size());
        for (EventRss eventRss : eventRssList) {
            eventItemList.add(new EventItem(eventRss, localDate, day));
        }
        return eventItemList;
    }
}
