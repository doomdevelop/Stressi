package org.wirbleibenalle.stressi.data.transformer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Preconditions;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.Event;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.wirbleibenalle.stressi.data.model.Event.NO_VALUE;


public class EventTransformer extends Transformer<Events, List<EventItem>> {
    private LocalDate localDate;
    private static Pattern PATTERN = Pattern.compile("[a-zA-Z]");

    @Override
    public List<EventItem> transform(Events events) {
        List<Event> eventList = events.events;
        List<EventItem> eventItemList = new ArrayList<>(eventList.size());
        String address, place, time, description;
        String[] addressAndDescription = null;

        for (Event event : eventList) {
            address = event.address;
            place = event.placeAddress;
            time = event.time;
            description = null;
            addressAndDescription = null;

            if (event.placeAndDescription != null) {
                addressAndDescription = event.placeAndDescription.split(":");
            }
            if (!isValid(address) || !containLetters(address)) {
                address = extractAddress(addressAndDescription);
            }
            description = extractDescription(addressAndDescription);

            if (!isValid(place) && isValid(address) && containLetters(address)) {
                place = address;
            }
            if (!isValid(address) && isValid(place) && containLetters(place)) {
                address = place;
            }
            if (!isValid(time)) {
                time = NO_VALUE;
            }
            if (!isValid(address) || !containLetters(address)) {
                address = NO_VALUE;
            }
            if (!isValid(place) || !containLetters(place)) {
                place = NO_VALUE;
            }
            if (!isValid(description) || !containLetters(description)) {
                description = NO_VALUE;
            }
            eventItemList.add(new EventItem(time, address, place, description, localDate));
        }
        return eventItemList;
    }

    @VisibleForTesting
    String extractAddress(@Nullable String[] addressAndDescription) {
        if (addressAndDescription != null && addressAndDescription.length > 0) {
            if (isValid(addressAndDescription[0]) &&
                containLetters(addressAndDescription[0])) {
                return addressAndDescription[0];
            }
        }
        return null;
    }

    @VisibleForTesting
    String extractDescription(@Nullable String[] addressAndDescription) {
        if (addressAndDescription == null || addressAndDescription.length <= 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < addressAndDescription.length; i++) {
            if (addressAndDescription[i] != null && addressAndDescription[i].length() > 0) {
                stringBuilder.append(addressAndDescription[i]);
            }
        }
        return stringBuilder.toString();
    }

    @VisibleForTesting
    boolean isValid(String s) {
        return s != null && !s.equals(NO_VALUE) && s.length() > 0;
    }

    @VisibleForTesting
    boolean containLetters(@NonNull String s) {
        return PATTERN.matcher(s).find();
    }

    public void setLocalDate(LocalDate localDate) {
        Preconditions.checkNotNull(localDate, "localDate can not be null!");
        this.localDate = localDate;
    }
}
