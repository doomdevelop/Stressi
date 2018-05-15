package org.wirbleibenalle.stressi.data.transformer;

import android.content.Context;
import android.content.res.Resources;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.wirbleibenalle.stressi.data.model.Event;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.wirbleibenalle.stressi.data.model.Event.NO_VALUE;

public class EventTransformerTest {

    private static String TIME = "12:20";
    private static String PLACE_ADDRESS = "placeAddress";
    private static String ADDRESS = "address";
    private static String DESCRIPTION = "description";
    private static String PLACE = "place";

    private static String ADDRESS_AND_DESCRIPTION = ADDRESS + ":" + DESCRIPTION;
    private static String[] ADDRESS_AND_DESCRIPTION_ARR = {ADDRESS, DESCRIPTION+"1",
        DESCRIPTION+"2"};
    private static String ADDRESS_AND_MULTI_DESCRIPTION = ADDRESS + ":" +
        ADDRESS_AND_DESCRIPTION_ARR[1]+":"+ ADDRESS_AND_DESCRIPTION_ARR[2];

    private static String[] ADDRESS_WITHOUT_DESCRIPTION = {"address"};

    private EventTransformer eventTransformer;
    private LocalDate localDate;

    private ArrayList<Event> createEventList(int length) {
        ArrayList<Event> events = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            events.add(createEvent());
        }
        return events;
    }

    private List<Event> createEventListCustom(int length, String address, String placeName, String placeAddress, String
        time, String placeAndDescription) {
        List<Event> events = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            events.add(createEvent(address, placeName, placeAddress, time, placeAndDescription));
        }
        return events;
    }

    private Event createEvent() {
        Event event = new Event();
        event.address = ADDRESS;
        event.placeAddress = PLACE_ADDRESS;
        event.time = TIME;
        event.placeAndDescription = ADDRESS_AND_DESCRIPTION;
        return event;
    }

    private Event createEvent(String address, String placeName, String placeAddress, String
        time, String placeAndDescription) {
        Event event = new Event();
        event.address = address;
        event.placeAddress = placeAddress;
        event.time = time;
        event.placeAndDescription = placeAndDescription;
        return event;
    }

    private void createLocalDate() {
        String date = "16/08/2016";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy");
        //convert String to LocalDate
        localDate = LocalDate.parse(date, formatter);
    }


    @Before
    public void setUp() throws Exception {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(resources.openRawResource(anyInt())).thenReturn(mock(InputStream.class));
        when(appContext.getResources()).thenReturn(resources);
        when(context.getApplicationContext()).thenReturn(appContext);
        JodaTimeAndroid.init(context);

        createLocalDate();
        eventTransformer = spy(new EventTransformer());
        eventTransformer.setLocalDate(localDate);
    }

    @Test
    public void transform_ShouldCreateEventItem_With_ValuesFromEvent() {
        Events events = new Events();
        List<Event> eventList = createEventList(1);
        events.events = eventList;
        Event event = events.events.get(0);

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(event.address, eventItem.getAddress());
        assertEquals(event.placeAddress, eventItem.getPlace());
        assertEquals(event.time, eventItem.getTime());
        assertTrue(event.placeAndDescription.contains(eventItem.getDescription()));
    }

    @Test
    public void transform_Should_CreateEventItem_With_No_Value() {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, null, null, null, null, null);
        events.events = eventList;

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getAddress(), NO_VALUE);
        assertEquals(eventItem.getPlace(), NO_VALUE);
        assertEquals(eventItem.getTime(), NO_VALUE);
        assertEquals(eventItem.getDescription(), NO_VALUE);
        assertEquals(localDate, eventItem.getLocalDate());
    }

    @Test
    public void transform_Should_CreateEventItem_With_LocalDate() {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, null, null, null, null, null);
        events.events = eventList;

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(localDate, eventItem.getLocalDate());
    }

    @Test
    public void transform_Should_CreateEventItem_With_AddressAsPlace_When_PlaceHasNull() {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, PLACE_ADDRESS, null, null, null, null);
        events.events = eventList;
        Event event = eventList.get(0);

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getAddress(), event.address);
        assertEquals(eventItem.getPlace(), event.address);
    }

    @Test
    public void transform_Should_CreateEventItem_With_AddressAsPlace_When_PlaceHasNoValue() {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, PLACE_ADDRESS, null, NO_VALUE, null,
            null);
        events.events = eventList;
        Event event = eventList.get(0);

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getAddress(), event.address);
        assertEquals(eventItem.getPlace(), event.address);
    }


    @Test
    public void transform_Should_CreateEventItem_With_PlaceAsAddress_When_AddressHasNoValue() {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, NO_VALUE, PLACE, NO_VALUE, null,
            null);
        events.events = eventList;
        Event event = eventList.get(0);

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getAddress(), event.placeAddress);
        assertEquals(eventItem.getPlace(), event.placeAddress);
    }

    @Test
    public void
    transform_Should_CreateEventItem_With_AddressAndDescription_From_AddressAndDescriptionForm
        () {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, NO_VALUE, null, null, null,
            ADDRESS_AND_DESCRIPTION);
        events.events = eventList;
        Event event = eventList.get(0);

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getAddress(), ADDRESS);
        assertEquals(eventItem.getPlace(), ADDRESS);
        assertEquals(eventItem.getDescription(), DESCRIPTION);
    }

    @Test
    public void
    transform_Should_CreateEventItem_With_AddressAndMultiDescription_From_AddressAndDescriptionForm
        () {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, NO_VALUE, null, null, null,
            ADDRESS_AND_MULTI_DESCRIPTION);
        events.events = eventList;
        Event event = eventList.get(0);

        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getAddress(), ADDRESS);
        assertEquals(eventItem.getPlace(), ADDRESS);
        assertFalse(eventItem.getDescription().contains(event.address));
        assertTrue(eventItem.getDescription().contains(ADDRESS_AND_DESCRIPTION_ARR[1]));
        assertTrue(eventItem.getDescription().contains(ADDRESS_AND_DESCRIPTION_ARR[2]));
    }

    @Test
    public void transform_Should_CreateEventItem_With_Time() {
        Events events = new Events();
        List<Event> eventList = createEventListCustom(1, NO_VALUE, NO_VALUE, NO_VALUE, TIME,
            NO_VALUE);
        events.events = eventList;
        Event event = eventList.get(0);
        List<EventItem> eventItemList = eventTransformer.transform(events);
        EventItem eventItem = eventItemList.get(0);

        assertEquals(eventItem.getTime(), event.time);
    }

    @Test
    public void isValid_Should_ReturnFalseIfNull() {
        assertFalse(eventTransformer.isValid(null));
    }

    @Test
    public void isValid_Should_ReturnFalseIfEmptyString() {
        assertFalse(eventTransformer.isValid(""));
    }

    @Test
    public void isValid_Should_ReturnFalseIfNO_VALUE() {
        assertFalse(eventTransformer.isValid(NO_VALUE));
    }

    @Test
    public void isValid_Should_ReturnTrueIfNotEmptyString() {
        assertTrue(eventTransformer.isValid("hej"));
    }

    @Test
    public void containLetters_Should_returnTrueWhenContainLetters() {
        assertTrue(eventTransformer.containLetters("hej123"));
    }

    @Test
    public void containLetters_Should_returnTrueWhenContainLettersUpperCase() {
        assertTrue(eventTransformer.containLetters("123HO123"));
    }

    @Test
    public void containLetters_Should_returnFalseWhenContainOnlyNumbers() {
        assertFalse(eventTransformer.containLetters("123"));
    }

    @Test
    public void containLetters_Should_returnFalseWhenContainNoLetterOtherChars() {
        assertFalse(eventTransformer.containLetters("@$§"));
    }

    @Test
    public void extractAddress_Should_ReturnNull_When_ParameterNull() {
        String address = eventTransformer.extractAddress(null);
        assertNull(address);
    }

    @Test
    public void extractAddress_Should_ReturnNull_When_ParameterEmptyArray() {
        String[] str = {};
        String address = eventTransformer.extractAddress(str);
        assertNull(address);
    }

    @Test
    public void extractAddress_Should_ExtractAddress() {
        String address = eventTransformer.extractAddress(ADDRESS_WITHOUT_DESCRIPTION);
        assertEquals(address, ADDRESS_WITHOUT_DESCRIPTION[0]);
    }

    @Test
    public void extractAddress_Should_ExtractCorrectAddress() {
        String address = eventTransformer.extractAddress(ADDRESS_AND_DESCRIPTION_ARR);
        assertEquals(address, ADDRESS_AND_DESCRIPTION_ARR[0]);
    }

    @Test
    public void extractDescription_Should_ReturnNull_When_ParameterNull() {
        String address = eventTransformer.extractDescription(null);
        assertNull(address);
    }

    @Test
    public void extractDescription_Should_ReturnNull_When_ParameterEmptyArray() {
        String[] str = {};
        String address = eventTransformer.extractDescription(str);
        assertNull(address);
    }

    @Test
    public void extractDescription_Should_ExtractCorrectDescription() {
        String description = eventTransformer.extractDescription(ADDRESS_AND_DESCRIPTION_ARR);
        assertFalse(description.contains(ADDRESS_AND_DESCRIPTION_ARR[0]));
        assertTrue(description.contains(ADDRESS_AND_DESCRIPTION_ARR[1]));
        assertTrue(description.contains(ADDRESS_AND_DESCRIPTION_ARR[2]));
    }
}