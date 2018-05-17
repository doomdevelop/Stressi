package org.wirbleibenalle.stressi.ui.main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.remote.handler.ConnectionHandler;
import org.wirbleibenalle.stressi.data.remote.handler.NetworkConnectionHandler;
import org.wirbleibenalle.stressi.domain.observer.BaseObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.ui.base.BasePresenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;
import org.wirbleibenalle.stressi.ui.model.ReceivedItems;
import org.wirbleibenalle.stressi.util.EventItemContentAnalyzer;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.wirbleibenalle.stressi.data.model.Event.NO_VALUE;
import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_BERLIN;
import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_LAT_LON;
import static org.wirbleibenalle.stressi.util.DateUtil.formatDateForTitle;

public class MainPresenter extends BasePresenter<MainActivityContract.View> implements
    MainActivityContract.Presenter {

    private final GetEventsUseCase getEventsUseCase;
    private final EventCacheController eventCacheController;
    private final CompositeDisposable compositeDisposable;
    private final NetworkConnectionHandler connectionHandler;
    private LocalDate currentLocalDate;
    private int currentPosition;
    private ReceivedItems receivedItems;

    private EventItemContentAnalyzer eventItemContentAnalyzer = new EventItemContentAnalyzer();

    private static final int DEFAULT_POSITION = Integer.MAX_VALUE / 2;
    private static final LocalDate DEFAULT_LOCAL_DATE = LocalDate.now();

    @Inject
    MainPresenter(GetEventsUseCase getEventsUseCase, EventCacheController eventCacheController,
                  NetworkConnectionHandler connectionHandler, CompositeDisposable
                      compositeDisposable) {
        this.getEventsUseCase = getEventsUseCase;
        this.eventCacheController = eventCacheController;
        this.connectionHandler = connectionHandler;
        this.compositeDisposable = compositeDisposable;
        setCurrentLocalDate(DEFAULT_LOCAL_DATE);
        setCurrentPosition(DEFAULT_POSITION);
    }

    @Override
    public void onPageSelected(int position) {
        onSwitchDateByPosition(position);
        if (isViewAttached()) {
            getView().showPullToRefreshProgress(position);
        }
        executeCall();
    }

    @VisibleForTesting
    void onSwitchDateByPosition(int position) {
        Timber.d("position=" + position + " currentPosition=" + currentPosition);
        if (position == currentPosition) {
            return;
        }
        if (position > currentPosition) {
            currentLocalDate = currentLocalDate.plusDays(position - currentPosition);
        } else {
            currentLocalDate = currentLocalDate.minusDays(currentPosition - position);
        }
        currentPosition = position;
        if (isViewAttached()) {
            getView().setDateToTitle(formatDateForTitle(currentLocalDate));
        }
    }

    void onPullToRefresh() {
        eventCacheController.setOnPullToRefresh(currentLocalDate.toString(), true);
        executeCall();
    }

    @VisibleForTesting
    void executeCall() {
        getEventsUseCase.setLocalDate(currentLocalDate);
        compositeDisposable.clear();
        receivedItems = null;
        getEventsUseCase.execute(new LoadEventObserver(currentPosition, connectionHandler),
            compositeDisposable);
    }

    void onAddEventToCalendar(EventItem eventItem) {
        DateTime datetime = eventItemContentAnalyzer.createEventDateTime(eventItem);

        String shortTitle = eventItemContentAnalyzer.createShortDescription(eventItem);

        if (isViewAttached()) {
            getView().addEventToCalendar(eventItem, datetime, shortTitle);
        }
    }

    void onShowEventOnMap(EventItem eventItem) {
        checkNotNull(eventItem, "EventItem can not be null!");
        if (!isViewAttached()) {
            return;
        }
        String address = eventItem.getAddress();
        if (address.equals(NO_VALUE)) {
            getView().showEventOnMap(GMM_INTENT_URI_LAT_LON + eventItem.getPlace());
        } else {
            String[] addresses = address.split(",");
            getView().showEventOnMap(addresses.length >= 2 ? GMM_INTENT_URI_LAT_LON + addresses[0] + GMM_INTENT_URI_BERLIN : GMM_INTENT_URI_LAT_LON + address);
        }
    }

    public void onShareEvent(@NonNull EventItem eventItem) {
        checkNotNull(eventItem);
        String shortDescription = eventItemContentAnalyzer.createShortDescription(eventItem);

        String subject = eventItemContentAnalyzer.createShareEventSubject(eventItem,
            shortDescription);

        String text = eventItemContentAnalyzer.createShareEventTextContent(eventItem);
        if (isViewAttached()) {
            if (subject == null && text == null) {
                getView().showShareEventError();
            } else {
                getView().shareEvent(subject, text);
            }
        }
    }

    class LoadEventObserver extends BaseObserver<List<EventItem>, ConnectionHandler,
        MainActivityContract.View> {
        private final int position;

        LoadEventObserver(int position, ConnectionHandler connectionHandler) {
            super(connectionHandler);
            this.position = position;
        }

        @Override
        public void onComplete() {
            //not used
        }

        @Override
        public boolean isViewAttached() {
            return MainPresenter.this.isViewAttached();
        }

        @Override
        public MainActivityContract.View getView() {
            return MainPresenter.this.getView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (!isViewAttached()) {
                return;
            }
            getView().hidePullToRefreshProgress(position);
        }

        @Override
        public void onNext(@NonNull List<EventItem> events) {
            Timber.d("onNext : position%s", position);
            receivedItems = new ReceivedItems(events, position);
            if (!isViewAttached()) {
                return;
            }
            getView().setItemsToRecycleView(events, position);
            getView().hidePullToRefreshProgress(position);
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    protected void onCreate() {
        if (isViewAttached()) {
            getView().setDateToTitle(formatDateForTitle(currentLocalDate));
            getView().initializeViewComponents(currentPosition, currentLocalDate);
            if (receivedItems != null) {
                if (receivedItems.position == currentPosition) {
                    getView().setItemsToRecycleView(receivedItems.items, receivedItems.position);
                    getView().hidePullToRefreshProgress(receivedItems.position);
                } else {
                    receivedItems = null;
                }
            }
        }
    }

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        executeCall();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("onCleared()");
        compositeDisposable.clear();
    }

    @VisibleForTesting
    LocalDate getCurrentLocalDate() {
        return currentLocalDate;
    }

    @VisibleForTesting
    void setCurrentLocalDate(LocalDate currentLocalDate) {
        this.currentLocalDate = currentLocalDate;
    }

    @VisibleForTesting
    int getCurrentPosition() {
        return currentPosition;
    }

    @VisibleForTesting
    void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @VisibleForTesting
    void setEventItemContentAnalyzer(EventItemContentAnalyzer eventItemContentAnalyzer) {
        this.eventItemContentAnalyzer = eventItemContentAnalyzer;
    }
}
