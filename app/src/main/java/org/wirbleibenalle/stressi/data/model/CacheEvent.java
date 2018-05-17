package org.wirbleibenalle.stressi.data.model;

public class CacheEvent {
    public final String htmlResponse;
    public final String date;
    public final long timestamp;

    public CacheEvent(String htmlResponse, String date, long timestamp) {
        this.htmlResponse = htmlResponse;
        this.date = date;
        this.timestamp = timestamp;
    }
}
