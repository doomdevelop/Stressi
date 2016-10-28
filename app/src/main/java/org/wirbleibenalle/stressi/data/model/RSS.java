package org.wirbleibenalle.stressi.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by and on 26.10.16.
 */
@Root(name = "rss", strict = false)
public class RSS {
    @Element(name = "channel")
    private ChannelRss channelRss;

    public ChannelRss getChannelRss() {
        return channelRss;
    }

    public RSS() {
    }

    public RSS(ChannelRss mChannelRss) {
        this.channelRss = mChannelRss;
    }

    @Override
    public String toString() {
        return "RSS{" +
            "channel=" + channelRss.toString() +
            '}';
    }
}
