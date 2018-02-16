package org.wirbleibenalle.stressi.data.transformer;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.Event;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.ArrayList;
import java.util.List;

import static org.wirbleibenalle.stressi.data.model.Event.NO_VALUE;

public class EventTransformer extends Transformer<Events, List<EventItem>> {
    private LocalDate localDate;

    @Override
    public List<EventItem> transform(Events events) {
        List<Event> eventList = events.events;
        List<EventItem> eventItemList = new ArrayList<>(eventList.size());
        String address, place, time, description;
        String[] placeAndDescription;

        for (Event event : eventList) {
            address = event.address;
            place = event.placeAddress;
            time = event.time;
            description = null;
            placeAndDescription = event.placeAndDescription.split(":");
            if (address == null || address.equals(NO_VALUE)) {
                if (placeAndDescription != null && placeAndDescription.length > 0) {
                    address = placeAndDescription[0];
                }
            } else {
                address = event.address;
            }
            if (placeAndDescription != null && placeAndDescription.length > 1) {
                for (int i = 1; i < placeAndDescription.length; i++) {
                    if (placeAndDescription[i] != null && placeAndDescription[i].length() >0) {
                        if (description == null) {
                            description = placeAndDescription[i];
                        } else {
                            description += placeAndDescription[i];
                        }
                    }
                }
            }
            if (place == null && address != null) {
                place = address;
            }
            time = time == null ? NO_VALUE : time;
            address = address == null ? NO_VALUE : address;
            place = place == null ? NO_VALUE : place;
            description = description == null ? NO_VALUE : description;
            eventItemList.add(new EventItem(time, address, place, description, localDate));
        }
        return eventItemList;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
