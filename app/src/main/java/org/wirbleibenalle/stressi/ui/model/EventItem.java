package org.wirbleibenalle.stressi.ui.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.ViewDebug;

import org.joda.time.LocalDate;

public class EventItem {
    private String time;
    private String address;
    private String place;
    private String description;
    private LocalDate localDate;

    @VisibleForTesting
    public EventItem(){
    }

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
