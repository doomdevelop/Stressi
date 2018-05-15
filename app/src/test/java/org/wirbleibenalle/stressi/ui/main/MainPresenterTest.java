package org.wirbleibenalle.stressi.ui.main;

import android.content.Context;
import android.content.res.Resources;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.remote.handler.ConnectionHandler;
import org.wirbleibenalle.stressi.data.remote.handler.NetworkConnectionHandler;
import org.wirbleibenalle.stressi.data.repository.DataRepository;
import org.wirbleibenalle.stressi.domain.observer.BaseObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.ui.model.EventItem;
import org.wirbleibenalle.stressi.util.DateUtil;
import org.wirbleibenalle.stressi.util.EventItemContentAnalyzer;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.disposables.CompositeDisposable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.wirbleibenalle.stressi.data.model.Event.NO_VALUE;
import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_BERLIN;
import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_LAT_LON;

/**
 * Created by and on 24.01.17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({CompositeDisposable.class, DateTime.class,BaseObserver.class})
public class MainPresenterTest {
    @Mock
    DataRepository dataRepository;
    @Mock
    MainActivityContract.View mainView;

    @Mock
    MainPresenter.LoadEventObserver loadEventObserver;

    @Mock
    EventItemContentAnalyzer eventItemContentAnalyzer;

    private final CompositeDisposable compositeDisposable = PowerMockito.mock(CompositeDisposable.class);

    private DateTime dateTime = PowerMockito.mock(DateTime.class);

    @Captor
    ArgumentCaptor<List<EventItem>> observerArgument;

    @Mock
    GetEventsUseCase mockGetEventsUseCase;
    @Mock
    EventCacheController eventCacheController;
    @Mock
    NetworkConnectionHandler connectionHandler;

    MainPresenter mainPresenter;

    private static final int POSITION = 1;
    private static final int NEXT_POSITION = 2;
    private static final int PREVIOUS_POSITION = -2;

    private static final String CORRECT_DATE = "2018-02-19";
    private static final int DEFAULT_POSITION = Integer.MAX_VALUE / 2;
    private LocalDate defaultLocalDate;
    private LocalDate defaultDate;
    private LocalDate nextDay;
    private LocalDate previousDay;

    private String TIME = "20:00";
    private String ADDRESS = "Berlin";
    private String PLACE = "Treptowerpark";
    private String DESCRIPTION = "This is event description";
    private String SHORT_DESCRIPTION = "This is event short description";
    private String timeoutExceptionMessage = "time out exception !";

    @Before
    public void setUp() throws Exception {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(resources.openRawResource(anyInt())).thenReturn(mock(InputStream.class));
        when(appContext.getResources()).thenReturn(resources);
        when(context.getApplicationContext()).thenReturn(appContext);

        JodaTimeAndroid.init(context);
        defaultLocalDate = LocalDate.now();
        defaultDate = defaultLocalDate;
        nextDay = defaultDate.plusDays(NEXT_POSITION);
        previousDay = defaultDate.minusDays(NEXT_POSITION);
        mainPresenter = new MainPresenter(mockGetEventsUseCase, eventCacheController,
            connectionHandler, compositeDisposable);
        mainPresenter.attachView(mainView);
    }

    private EventItem createEventItemMock() {
        EventItem eventItem = mock(EventItem.class);
        when(eventItem.getTime()).thenReturn(TIME);
        when(eventItem.getAddress()).thenReturn(ADDRESS);
        when(eventItem.getPlace()).thenReturn(PLACE);
        when(eventItem.getDescription()).thenReturn(DESCRIPTION);
        when(eventItem.getLocalDate()).thenReturn(defaultLocalDate);
        return eventItem;
    }

    @Test
    public void onPageSelected_PresenterShouldNotCrash_When_ViewDetached() {
        mainPresenter.detachView();
        mainPresenter.onPageSelected(POSITION);
        verify(mainView, never()).showPullToRefreshProgress(POSITION);
    }

    @Test
    public void onPageSelected_ViewShouldShowProgress() {
        mainPresenter.attachView(mainView);
        mainPresenter.onPageSelected(POSITION);
        verify(mainView).showPullToRefreshProgress(POSITION);
    }

    @Test
    public void onSwitchDateByPosition_ShouldIgnoreCall_When_GivingPositionEqualsCurrentPosition() {
        mainPresenter.attachView(mainView);
        mainPresenter.setCurrentPosition(DEFAULT_POSITION);
        mainPresenter.onSwitchDateByPosition(DEFAULT_POSITION);
        verify(mainView, never()).setDateToTitle(anyString());
    }

    @Test
    public void onSwitchDateByPosition_ShouldCalculateNextCorrectDate_And_SetInTitle() {
        mainPresenter.attachView(mainView);
        mainPresenter.setCurrentPosition(DEFAULT_POSITION);
        mainPresenter.setCurrentLocalDate(defaultDate);

        mainPresenter.onSwitchDateByPosition(DEFAULT_POSITION + NEXT_POSITION);
        Assert.assertEquals(mainPresenter.getCurrentLocalDate(), nextDay);
        verify(mainView).setDateToTitle(DateUtil.formatDateForTitle(nextDay));
    }

    @Test
    public void onSwitchDateByPosition_ShouldCalculatePreviousCorrectDate_And_SetInTitle() {
        mainPresenter.attachView(mainView);
        mainPresenter.setCurrentPosition(DEFAULT_POSITION);
        mainPresenter.setCurrentLocalDate(defaultDate);

        mainPresenter.onSwitchDateByPosition(DEFAULT_POSITION - NEXT_POSITION);
        Assert.assertEquals(mainPresenter.getCurrentLocalDate(), previousDay);
        verify(mainView).setDateToTitle(DateUtil.formatDateForTitle(previousDay));
    }

    @Test
    public void onPullToRefresh_Should_SetFlag() {
        mainPresenter.attachView(mainView);
        mainPresenter.setCurrentPosition(DEFAULT_POSITION);
        mainPresenter.setCurrentLocalDate(defaultDate);

        mainPresenter.onPullToRefresh();
        verify(eventCacheController).setOnPullToRefresh(defaultDate.toString(), true);
    }

    @Test
    public void executeCall_Should_ExecuteCallInUseCase() {
        mainPresenter.attachView(mainView);
        mainPresenter.setCurrentPosition(DEFAULT_POSITION);
        mainPresenter.setCurrentLocalDate(defaultDate);

        mainPresenter.onPullToRefresh();
        verify(mockGetEventsUseCase).execute(any(BaseObserver.class),
            any(CompositeDisposable.class));
    }

    @Test
    public void executeCall_Should_SetCurrentDateInUseCase() {
        mainPresenter.attachView(mainView);
        mainPresenter.setCurrentPosition(DEFAULT_POSITION);
        mainPresenter.setCurrentLocalDate(defaultDate);

        mainPresenter.onPullToRefresh();
        verify(mockGetEventsUseCase).setLocalDate(defaultDate);
    }

    @Test
    public void executeCall_Should_ClearCompositeDisposable() {
        mainPresenter.executeCall();
        verify(compositeDisposable).clear();
    }

    @Test
    public void onAddEventToCalendar_Should_CreateEventDateTime() {
        EventItem eventItem = createEventItemMock();
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        mainPresenter.onAddEventToCalendar(eventItem);
        verify(eventItemContentAnalyzer).createEventDateTime(eventItem);
    }

    @Test
    public void onAddEventToCalendar_Should_CreateShortDescription() {
        EventItem eventItem = createEventItemMock();
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        mainPresenter.onAddEventToCalendar(eventItem);
        verify(eventItemContentAnalyzer).createShortDescription(eventItem);
    }

    @Test
    public void onAddEventToCalendar_Should_AddEventToCalendar() {
        mainPresenter.attachView(mainView);
        EventItem eventItem = createEventItemMock();
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        when(eventItemContentAnalyzer.createEventDateTime(eventItem)).thenReturn(dateTime);
        when(eventItemContentAnalyzer.createShortDescription(eventItem)).thenReturn(SHORT_DESCRIPTION);

        mainPresenter.onAddEventToCalendar(eventItem);

        verify(mainView).addEventToCalendar(eventItem, dateTime, SHORT_DESCRIPTION);
    }

    @Test
    public void onShowEventOnMap_Should_CallShowEventOnMapWithPlace_When_NoAddress() {
        EventItem eventItem = createEventItemMock();
        when(eventItem.getAddress()).thenReturn(NO_VALUE);
        String place = GMM_INTENT_URI_LAT_LON + eventItem.getPlace();
        mainPresenter.attachView(mainView);
        mainPresenter.onShowEventOnMap(eventItem);
        verify(mainView).showEventOnMap(place);
    }

    @Test
    public void onShowEventOnMap_Should_CallShowEventOnMapWithAddress_When_ValidAddress() {
        EventItem eventItem = createEventItemMock();
        String place = GMM_INTENT_URI_LAT_LON + eventItem.getAddress();
        mainPresenter.attachView(mainView);
        mainPresenter.onShowEventOnMap(eventItem);
        verify(mainView).showEventOnMap(place);
    }

    @Test
    public void onShowEventOnMap_Should_CallShowEventOnMapWithAddress_When_AddressWithMoreData() {
        EventItem eventItem = createEventItemMock();
        String address = eventItem.getAddress();
        String addressWithMoreData = address + ",Mitte, Subversive";
        when(eventItem.getAddress()).thenReturn(addressWithMoreData);
        String place = GMM_INTENT_URI_LAT_LON + address + GMM_INTENT_URI_BERLIN;
        mainPresenter.attachView(mainView);
        mainPresenter.onShowEventOnMap(eventItem);
        verify(mainView).showEventOnMap(place);
    }

    @Test
    public void onShareEvent_Should_CreateShortDescription(){
        EventItem eventItem = createEventItemMock();
        mainPresenter.attachView(mainView);
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        mainPresenter.onShareEvent(eventItem);
        verify(eventItemContentAnalyzer).createShortDescription(eventItem);
    }

    @Test
    public void onShareEvent_Should_CreateShareEventSubject(){
        EventItem eventItem = createEventItemMock();
        String shortDescription = "short description";
        when(eventItemContentAnalyzer.createShortDescription(eventItem)).thenReturn
            (shortDescription);

        mainPresenter.attachView(mainView);
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        mainPresenter.onShareEvent(eventItem);

        verify(eventItemContentAnalyzer).createShareEventSubject(eventItem,shortDescription);
    }

    @Test
    public void onShareEvent_Should_ShowShareEventError_WhenNoSubjectAndNoTextToShare(){
        EventItem eventItem = createEventItemMock();
        String shortDescription = "short description";
        when(eventItemContentAnalyzer.createShortDescription(eventItem)).thenReturn
            (null);

        mainPresenter.attachView(mainView);
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        mainPresenter.onShareEvent(eventItem);

        verify(eventItemContentAnalyzer,never()).createShareEventSubject(eventItem,shortDescription);
        verify(mainView).showShareEventError();
    }

    @Test
    public void onShareEvent_Should_CreateShareEventTextContent(){
        EventItem eventItem = createEventItemMock();

        mainPresenter.attachView(mainView);
        mainPresenter.setEventItemContentAnalyzer(eventItemContentAnalyzer);
        mainPresenter.onShareEvent(eventItem);

        verify(eventItemContentAnalyzer).createShareEventTextContent(eventItem);
    }

    @Test
    public void
    onError_LoadEventObserver_Should_callViewWith_NetworkErrorMessage_When_NoNetworkConnection(){
        ConnectionHandler connectionHandler = mock(ConnectionHandler.class);
        when(connectionHandler.hasConnection()).thenReturn(false);

        MainPresenter.LoadEventObserver loadEventObserver = spy(mainPresenter.new LoadEventObserver(DEFAULT_POSITION,connectionHandler));

        when(loadEventObserver.getConnectionHandler()).thenReturn(connectionHandler);
        when(loadEventObserver.getView()).thenReturn(mainView);
        when(loadEventObserver.isViewAttached()).thenReturn(true);
        loadEventObserver.onError(any(Throwable.class));
        verify(mainView).showNoConnectionErrorMessage();
    }

    @Test
    public void onError_LoadEventObserver_Should_showError_When_SpecificExceptionThrow(){
        ConnectionHandler connectionHandler = mock(ConnectionHandler.class);
        when(connectionHandler.hasConnection()).thenReturn(true);
        TimeoutException timeoutException = mock(TimeoutException.class);
        when(timeoutException.getMessage()).thenReturn(timeoutExceptionMessage);

        MainPresenter.LoadEventObserver loadEventObserver = spy(mainPresenter.new LoadEventObserver(DEFAULT_POSITION,connectionHandler));

        when(loadEventObserver.getConnectionHandler()).thenReturn(connectionHandler);
        when(loadEventObserver.getView()).thenReturn(mainView);
        when(loadEventObserver.isViewAttached()).thenReturn(true);
        loadEventObserver.onError(timeoutException);
        verify(mainView).showError(timeoutException.getMessage());
    }

    @Test
    public void onError_LoadEventObserver_Should_hidePullToRefreshProgress(){
        ConnectionHandler connectionHandler = mock(ConnectionHandler.class);
        when(connectionHandler.hasConnection()).thenReturn(false);

        MainPresenter.LoadEventObserver loadEventObserver = spy(mainPresenter.new LoadEventObserver(DEFAULT_POSITION,connectionHandler));

        when(loadEventObserver.getView()).thenReturn(mainView);
        when(loadEventObserver.isViewAttached()).thenReturn(true);
        when(loadEventObserver.getConnectionHandler()).thenReturn(connectionHandler);
        loadEventObserver.onError(any(Throwable.class));
        verify(mainView).hidePullToRefreshProgress(anyInt());
    }

    @Test
    public void onCleared_Should_clearcompositeDisposable(){
        mainPresenter.onCleared();
        verify(compositeDisposable).clear();
    }
}