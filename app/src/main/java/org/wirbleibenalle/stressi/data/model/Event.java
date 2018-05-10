package org.wirbleibenalle.stressi.data.model;

import pl.droidsonroids.jspoon.annotation.Selector;

public class Event {
    public static final String NO_VALUE = "NO_VALUE";

    @Selector(".uhrzeit2")
    public String time;
    @Selector(".spalte_termintext > b > a")
    public String placeName;
    //optional if address NO_VALUE
    @Selector(".spalte_termintext > b")
    public String placeAddress;
    //if address NO_VALUE use placeAddress
    @Selector(value = ".spalte_termintext > b > a", attr = "title")
    public String address;
    @Selector(".spalte_termintext")
    public String placeAndDescription;
    @Selector(value = ".spalte_termintext")
    public String eventTitle;
}
