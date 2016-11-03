package org.wirbleibenalle.stressi.ui.model;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.EventRss;

/**
 * Created by and on 27.10.16.
 */

public class EventItem {
    private String title;
    private String link;
    private String guid;
    private String pubDate;
    private String description;
    private Integer day;
    private LocalDate localDate;

    public EventItem() {

    }

    public EventItem(EventRss eventRss) {
        setDescription(eventRss.getDescription());
        setGuid(eventRss.getGuid());
        setLink(eventRss.getLink());
        setPubDate(eventRss.getPubDate());
        setTitle(eventRss.getTitle());
    }

    public EventItem(EventRss eventRss, LocalDate localDate, Integer day) {
        setDescription(eventRss.getDescription());
        setGuid(eventRss.getGuid());
        setLink(eventRss.getLink());
        setPubDate(eventRss.getPubDate());
        setTitle(eventRss.getTitle());
        setLocalDate(localDate);
        setDay(day);
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

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
