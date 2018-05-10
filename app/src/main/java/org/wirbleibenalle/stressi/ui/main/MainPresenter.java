package org.wirbleibenalle.stressi.ui.main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.remote.ErrorHandler;
import org.wirbleibenalle.stressi.data.remote.ResponseError;
import org.wirbleibenalle.stressi.data.remote.ResponseErrorListener;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.ui.base.BasePresenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;
import org.wirbleibenalle.stressi.ui.model.ReceivedItems;
import org.wirbleibenalle.stressi.util.Constants;
import org.wirbleibenalle.stressi.util.DateUtil;
import org.wirbleibenalle.stressi.util.EventItemContentAnalyzer;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_BERLIN;
import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_LAT_LON;
import static org.wirbleibenalle.stressi.util.EventItemContentAnalyzer.createShortDescription;

public class MainPresenter extends BasePresenter<MainActivityContract.View> implements
    MainActivityContract.Presenter, ResponseErrorListener {

    private final GetEventsUseCase getEventsUseCase;
    private final EventCacheController eventCacheController;
    private final ErrorHandler errorHandler;
    private LocalDate currentLocalDate;
    private int currentPosition;
    private final CompositeDisposable compositeDisposable;
    private ReceivedItems receivedItems;

    @Inject
    public MainPresenter(GetEventsUseCase getEventsUseCase, EventCacheController eventCacheController, ErrorHandler errorHandler) {
        this.getEventsUseCase = getEventsUseCase;
        this.eventCacheController = eventCacheController;
        this.errorHandler = errorHandler;
        this.compositeDisposable = new CompositeDisposable();
        initialize();
    }

    public void initialize() {
        currentLocalDate = LocalDate.now();
        currentPosition = Integer.MAX_VALUE / 2;
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
        Timber.d( "position=" + position + " currentPosition=" + currentPosition);
        if (position == currentPosition) {
            return;
        }
        if (position > currentPosition) {
            currentLocalDate = currentLocalDate == null ? LocalDate.now().plusDays(position) :
                currentLocalDate.plusDays(position - currentPosition);
        } else {
            currentLocalDate = currentLocalDate.minusDays(currentPosition - position);
        }
        currentPosition = position;
        if (isViewAttached()) {
            getView().setDateToTitle(DateUtil.formatDateForTitle(currentLocalDate));
        }
    }

    void onPullToRefresh() {
        eventCacheController.setOnPullToRefresh(currentLocalDate.toString(), true);
        executeCall();
    }

    @VisibleForTesting
    void executeCall() {
        getEventsUseCase.setLocalDate(currentLocalDate);
        getEventsUseCase.setPosition(currentPosition);
        compositeDisposable.clear();
        getEventsUseCase.execute(new LoadEventObserver(currentPosition));
    }

    void onAddEventToCalendar(EventItem eventItem) {
        LocalTime localTime = new LocalTime();
        String[] hourMin = eventItem.getTime().split(":");
        localTime.hourOfDay().setCopy(Integer.valueOf(hourMin[0]));
        localTime.minuteOfHour().setCopy(Integer.valueOf(hourMin[1]));
        localTime.secondOfMinute().setCopy(0);

        DateTime datetime = eventItem.getLocalDate().toDateTime(localTime);

        String shortTitle = EventItemContentAnalyzer.createShortDescription(eventItem);
        if (isViewAttached()) {
            getView().addEventToCalendar(eventItem, datetime, shortTitle);
        }
    }

    void onShowEventOnMap(EventItem eventItem) {
        if (!isViewAttached()) {
            return;
        }
        String address = eventItem.getAddress();
        if (address == null) {
            getView().showEventOnMap(GMM_INTENT_URI_LAT_LON + eventItem.getPlace());
        } else {
            String[] adresses = address.split(",");
            getView().showEventOnMap(adresses.length >= 2 ? GMM_INTENT_URI_LAT_LON + adresses[0] + GMM_INTENT_URI_BERLIN : GMM_INTENT_URI_LAT_LON + address);
        }
    }

    public void onShareEvent(EventItem eventItem) {
        String descriptionShort = createShortDescription(eventItem);

        String subject = eventItem.getPlace() + ": " + descriptionShort;
        StringBuilder textBuilder = new StringBuilder(eventItem.getPlace());

        if (!eventItem.getPlace().equals(eventItem.getAddress())) {
            textBuilder.append("\n").append(eventItem.getAddress());
        }
        textBuilder.append("\n\n").append(DateUtil.formatDateForTitle(eventItem.getLocalDate()))
            .append(" ").append(eventItem.getTime());
        textBuilder.append("\n\n").append(eventItem.getDescription())
            .append("\n\n").append(Constants.FULL_URL).append(eventItem.getLocalDate().toString());
        String text = textBuilder.toString();
        if (isViewAttached()) {
            getView().shareEvent(subject, text);
        }
    }


    @Override
    public void onResponseError(ResponseError responseError) {
        if (!isViewAttached()) {
            return;
        }
        switch (responseError.getErrorType()) {
            case ResponseError.ERROR_NETWORK_CONNECTION:
                getView().showNoConnectionErrorMessage();
                break;
            case ResponseError.ERROR_UNDEFINED:
                if (responseError.getErrorCode() == 500) {
                    //TODO :  server error
                } else if (responseError.getErrorCode() == 401) {
                    //TODO:  resource we're trying to access is not available
                }
                break;
        }
    }

    public class LoadEventObserver implements Observer<List<EventItem>> {
        private final int position;

        public LoadEventObserver(int position) {
            this.position = position;
        }

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().showError(e.getMessage());
            getView().hidePullToRefreshProgress(position);
        }

        @Override
        public void onSubscribe(Disposable d) {
            receivedItems = null;
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(List<EventItem> events) {
            Timber.d( "onNext : position" + position);
            receivedItems = new ReceivedItems(events,position);
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
            getView().setDateToTitle(DateUtil.formatDateForTitle(currentLocalDate));
            getView().initializeViewComponents(currentPosition, currentLocalDate);
            if(receivedItems != null) {
                if(receivedItems.position == currentPosition) {
                    getView().setItemsToRecycleView(receivedItems.items, receivedItems.position);
                    getView().hidePullToRefreshProgress(receivedItems.position);
                }else{
                    receivedItems = null;
                }
            }
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        //not used
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    public void onStart() {
        errorHandler.addResponseErrorListener(this);
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_STOP)
    public void onStop() {
        errorHandler.removeResponseErrorListener(this);
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
        errorHandler.removeResponseErrorListener(this);
        compositeDisposable.clear();
    }
}
