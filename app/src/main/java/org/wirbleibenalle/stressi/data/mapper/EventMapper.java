package org.wirbleibenalle.stressi.data.mapper;

import org.wirbleibenalle.stressi.data.model.EventRss;
import org.wirbleibenalle.stressi.data.model.RSS;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by and on 28.10.16.
 */

public class EventMapper {
    public List<EventItem> transform(RSS rss) {
        List<EventRss> eventRssList = rss.getChannelRss().getFeedItems();
        List<EventItem> eventItemList = new ArrayList<>(eventRssList.size());
        for (EventRss eventRss : eventRssList) {
            eventItemList.add(new EventItem(eventRss));
        }
        return eventItemList;
    }
}
