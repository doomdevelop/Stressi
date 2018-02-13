package org.wirbleibenalle.stressi.ui.model;

import org.joda.time.LocalDate;

/**
 * Created by and on 27.10.16.
 */

public class EventItem {
    private final String time;
    private final String address;
    private final String place;
    private final String description;

    public EventItem(String time,  String address, String place, String description) {
        this.time = time;
        this.address = address;
        this.place = place;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public String getPlace() {
        return place;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "EventItem{" +
            "time='" + time + '\'' +
            ", address='" + address + '\'' +
            ", place='" + place + '\'' +
            ", description='" + description + '\'' +
                '}';
    }
}
