package org.wirbleibenalle.stressi.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by and on 26.10.16.
 */
@Root(name = "item", strict = false)
public class EventRss {
    @Element(name = "title")
    private String title;
    @Element(name = "link")
    private String link;
    @Element(name = "guid", required = false)
    private String guid;
    @Element(name = "pubDate")
    private String pubDate;
    @Element(name = "description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EventItem{" +
            "title='" + title + '\'' +
            ", link='" + link + '\'' +
            ", guid='" + guid + '\'' +
            ", pubDate='" + pubDate + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
