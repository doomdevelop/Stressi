package org.wirbleibenalle.stressi.data.model;

import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

public class Events {
    @Selector(".termin_box")public List<Event> events;
}
