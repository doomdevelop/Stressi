package org.wirbleibenalle.stressi.ui.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * Created by and on 27.10.16.
 */

public class EventItem {
    private final String time;
    private final String address;
    private final String place;
    private final String description;
    private final LocalDate localDate;

    public EventItem(@NonNull String time,@NonNull String address,@NonNull String place,@NonNull String description, @Nullable LocalDate localDate) {
        this.time = time;
        this.address = address;
        this.place = place;
        this.description = description;
        this.localDate = localDate;
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

    public LocalDate getLocalDate() {
        return localDate;
    }
}
