package org.wirbleibenalle.stressi.ui.main;

import android.content.Context;
import android.content.res.Resources;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.runners.MockitoJUnitRunner;
import org.wirbleibenalle.stressi.data.model.EventRss;
import org.wirbleibenalle.stressi.data.repository.DataRepository;
import org.wirbleibenalle.stressi.domain.observer.DefaultObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.domain.usecase.UseCase;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.wirbleibenalle.stressi.ui.main.MainPresenter.*;

/**
 * Created by and on 24.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    @Mock
    private DataRepository dataRepository;
    @Mock
    private MainView mainView;

    @Mock
    private MainPresenter.LoadEventObserver loadEventObserver;

    @Captor
    private ArgumentCaptor<List<EventItem>> observerArgument;

    @Mock
    private GetEventsUseCase mockGetEventsUseCase;
    private MainPresenter mainPresenter;


    @Before
    public void setUp() throws Exception {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(resources.openRawResource(anyInt())).thenReturn(mock(InputStream.class));
        when(appContext.getResources()).thenReturn(resources);
        when(context.getApplicationContext()).thenReturn(appContext);
        JodaTimeAndroid.init(context);
        mainPresenter = new MainPresenter(mockGetEventsUseCase);
        mainPresenter.setView(mainView);
    }



    @Test
    public void testSwitchDateByPosition(){
        LocalDate nowLocalDate = LocalDate.now();
        int day = 1;
        List<EventItem> evenItemList = new ArrayList<>();
        EventRss eventRss = new EventRss();
        eventRss.setDescription("Description");
        eventRss.setGuid("000000");
        eventRss.setLink("http");
        eventRss.setPubDate("date");
        eventRss.setTitle("title");
        evenItemList.add(new EventItem(eventRss,nowLocalDate.plusDays(day),day));
        mainPresenter.onSwitchDateByPosition(1);
        verify(mockGetEventsUseCase).execute(any(Observer.class));
    }

    @Test
    public void testCovertEventRssToEventItem(){
        LocalDate nowLocalDate = LocalDate.now();
        Integer day = 1;
        String description = "Description";
        String guid = "Guid";
        String link = "Link";
        String pubDate = "PubDate";
        String title ="title";
        EventRss eventRss = new EventRss();
        eventRss.setDescription(description);
        eventRss.setGuid(guid);
        eventRss.setLink(link);
        eventRss.setPubDate(pubDate);
        eventRss.setTitle(title);

        EventItem eventItem = new EventItem(eventRss,nowLocalDate,day);
        assertEquals(eventItem.getDescription(),description);
        assertEquals(eventItem.getTitle(),title);
        assertEquals(eventItem.getGuid(),guid);
        assertEquals(eventItem.getLink(),link);
        assertEquals(eventItem.getPubDate(),pubDate);
        assertEquals(eventItem.getDay(),day);

    }
}