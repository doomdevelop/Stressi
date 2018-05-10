package org.wirbleibenalle.stressi.ui.model;

import java.util.List;

public class ReceivedItems {
    public final List<EventItem> items;
    public final int position;

    public ReceivedItems(List<EventItem> items, int position) {
        this.items = items;
        this.position = position;
    }
}
