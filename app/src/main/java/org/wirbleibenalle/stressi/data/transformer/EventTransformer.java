package org.wirbleibenalle.stressi.data.transformer;

import org.wirbleibenalle.stressi.data.model.Event;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.ArrayList;
import java.util.List;

public class EventTransformer extends Transformer<Events, List<EventItem>> {
    @Override
    public List<EventItem> transform(Events events) {
        List<Event> eventList = events.events;
        List<EventItem> eventItemList = new ArrayList<>(eventList.size());
        String address;
        String place;
        for (Event event : eventList) {
            address = event.address;
            place = event.placeAddress;
            String[] placeAndDescription = event.placeAndDescription.split(":");
            String description = "";
            if (event.address == null || address.equals(Event.NO_VALUE)) {
                if (placeAndDescription.length > 0) {
                    address = placeAndDescription[0];
                }
            } else {
                address = event.address;
            }
            if (placeAndDescription.length > 1) {
                for (int i = 1; i < placeAndDescription.length; i++) {
                    if (placeAndDescription[i] != null) {
                        description += placeAndDescription[i];
                    }
                }
            }

            eventItemList.add(new EventItem(event.time, address, place, description));
        }
        return eventItemList;
    }
}
