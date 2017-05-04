package org.wirbleibenalle.stressi.data.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by and on 26.10.16.
 */

@Root(name = "channel", strict = false)
public class ChannelRss {
    @ElementList(required = false,empty = false, inline = true, name = "item")
    private List<EventRss> eventRssList;

    public List<EventRss> getFeedItems() {
        return eventRssList;
    }

    public ChannelRss() {
    }

    public ChannelRss(List<EventRss> eventRssList) {
        this.eventRssList = eventRssList;
    }

    @Override
    public String toString() {
        return "Channel{" +
            "mFeedItems=" + eventRssList +
            '}';
    }
}
