package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.remote.ResponseError;
import org.wirbleibenalle.stressi.data.remote.ResponseException;
import org.wirbleibenalle.stressi.domain.observer.DefaultObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.ui.base.Presenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;
import org.wirbleibenalle.stressi.util.EventItemContentAnalyzer;

import java.util.List;

import javax.inject.Inject;

import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_LAT_LON;
import static org.wirbleibenalle.stressi.util.Constants.GMM_INTENT_URI_BERLIN;
import static org.wirbleibenalle.stressi.util.EventItemContentAnalyzer.createShortDescription;

/**
 * Created by and on 26.10.16.
 */
public class MainPresenter extends Presenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private final GetEventsUseCase getEventsUseCase;
    private final EventCacheController eventCacheController;
    private LocalDate currentLocalDate;
    private int currentPosition;


    @Inject
    public MainPresenter(GetEventsUseCase getEventsUseCase, EventCacheController eventCacheController) {
        this.getEventsUseCase = getEventsUseCase;
        this.eventCacheController = eventCacheController;
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        currentLocalDate = LocalDate.now();
        currentPosition = Integer.MAX_VALUE / 2;
        if (view != null) {
            view.setDateToTitle(currentLocalDate.toString());
            view.initializeViewComponents(currentPosition);
        }
        executeCall();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    void onDestroy() {
        getEventsUseCase.unsubscribe();
    }

    void onPageSelected(int position) {
        onSwitchDateByPosition(position);
        if (view != null) {
            view.showPullToRefreshProgress(position);
        }
        executeCall();
    }

    @VisibleForTesting
    void onSwitchDateByPosition(int position) {
        Log.d(TAG, "position=" + position + " currentPosition=" + currentPosition);
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
        if (view != null) {
            view.setDateToTitle(currentLocalDate.toString());
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
        getEventsUseCase.unsubscribe();
        getEventsUseCase.execute(new LoadEventObserver(currentLocalDate, currentPosition));
    }

    void onAddEventToCalendar(EventItem eventItem) {
        LocalTime localTime = new LocalTime();
        String[] hourMin = eventItem.getTime().split(":");
        localTime.hourOfDay().setCopy(Integer.valueOf(hourMin[0]));
        localTime.minuteOfHour().setCopy(Integer.valueOf(hourMin[1]));
        localTime.secondOfMinute().setCopy(0);

        DateTime datetime = eventItem.getLocalDate().toDateTime(localTime);

        String shortTitle = EventItemContentAnalyzer.createShortDescription(eventItem);
        if (view != null) {
            view.addEventToCalendar(eventItem, datetime, shortTitle);
        }
    }

    void onShowEventOnMap(EventItem eventItem) {
        if (view == null) {
            return;
        }
        String address = eventItem.getAddress();
        if (address == null) {
            view.showEventOnMap(GMM_INTENT_URI_LAT_LON + eventItem.getPlace());
        } else {
            String[] adresses = address.split(",");
            view.showEventOnMap(adresses.length >= 2 ? GMM_INTENT_URI_LAT_LON + adresses[0] + GMM_INTENT_URI_BERLIN : GMM_INTENT_URI_LAT_LON + address);
        }
    }

    public void onShareEvent(EventItem eventItem) {
        String descriptionShort = createShortDescription(eventItem);

        String subject = eventItem.getPlace() + ": " + descriptionShort;
        StringBuilder textBuilder = new StringBuilder(eventItem.getPlace());
        textBuilder.append(" ").append(eventItem.getTime()).append("\n");
        if (!eventItem.getPlace().equals(eventItem.getAddress())) {
            textBuilder.append(eventItem.getAddress());
        }
        textBuilder.append("\n\n").append(eventItem.getDescription());
        String text = textBuilder.toString();
        if (view != null) {
            view.shareEvent(subject, text);
        }
    }

    public class LoadEventObserver extends DefaultObserver<List<EventItem>> {
        private final int position;
        private final LocalDate localDate;

        public LoadEventObserver(LocalDate localDate, int position) {
            this.position = position;
            this.localDate = localDate;
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (view == null) {
                return;
            }
            if (e instanceof ResponseException) {
                ResponseException responseException = (ResponseException) e;
                switch (responseException.getResponseError().getErrorType()) {
                    case ResponseError.ERROR_NETWORK_CONNECTION:
                        view.showNoConnectionErrorMessage();
                        break;
                    case ResponseError.ERROR_UNDEFINED:
                        if (responseException.getResponseError().getErrorCode() == 500) {
                            //TODO :  server error
                        } else if (responseException.getResponseError().getErrorCode() == 401) {
                            //TODO:  resource we're trying to access is not available
                        }
                        break;
                }
            } else {
                //TODO: generic error
            }
            view.hidePullToRefreshProgress(position);
        }

        @Override
        public void onNext(List<EventItem> events) {
            super.onNext(events);
            Log.d(TAG, "onNext : position" + position);
            if (view == null) {
                return;
            }
            view.setItemsToRecycleView(events, position);
            view.hidePullToRefreshProgress(position);
        }
    }
}
