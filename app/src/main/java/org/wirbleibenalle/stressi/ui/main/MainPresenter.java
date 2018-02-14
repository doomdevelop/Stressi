package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.util.Log;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.domain.observer.DefaultObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.ui.base.Presenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by and on 26.10.16.
 */

public class MainPresenter extends Presenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private final GetEventsUseCase getEventsUseCase;
    private LocalDate currentLocalDate;
    private int currentPosition;


    @Inject
    public MainPresenter(GetEventsUseCase getEventsUseCase) {
        this.getEventsUseCase = getEventsUseCase;
    }

    void onPageSelected(int position){
        onSwitchDateByPosition(position);
        view.showPullToRefreshProgress(position);
        loadEvents();
    }
    @VisibleForTesting
    void onSwitchDateByPosition(int position) {
        Log.d(TAG,"position="+position+" currentPosition="+currentPosition);
        if(position == currentPosition){
            return;
        }
        if (position > currentPosition) {
            currentLocalDate = currentLocalDate == null ? LocalDate.now().plusDays(position) :
                currentLocalDate.plusDays(position-currentPosition) ;
        } else {
            currentLocalDate = currentLocalDate.minusDays(currentPosition-position);
        }
        currentPosition = position;
        view.setDateToTitle(currentLocalDate.toString());
    }

    void loadEvents() {
        executeCall();
    }

    @VisibleForTesting
    void executeCall(){
        getEventsUseCase.setLocalDate(currentLocalDate);
        getEventsUseCase.setPosition(currentPosition);
        getEventsUseCase.unsubscribe();
        getEventsUseCase.execute(new LoadEventObserver(currentLocalDate,currentPosition));
    }

    public class LoadEventObserver extends DefaultObserver<List<EventItem>>{
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
            Log.d(TAG, e.getMessage());
            view.hidePullToRefreshProgress(position);
            //TODO: implement error case
        }

        @Override
        public void onNext(List<EventItem> events) {
            super.onNext(events);
            Log.d(TAG, "onNext : position" + position);
            view.setItemsToRecycleView(events, position);
            view.hidePullToRefreshProgress(position);
        }
    }
    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        currentLocalDate = LocalDate.now();
        currentPosition = Integer.MAX_VALUE / 2;
        view.setDateToTitle(currentLocalDate.toString());
        view.initializeViewComponents(currentPosition);
        loadEvents();
    }
}
