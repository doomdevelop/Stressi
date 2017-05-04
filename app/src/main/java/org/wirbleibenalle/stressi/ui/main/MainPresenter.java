package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.util.Log;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.repository.DataRepository;
import org.wirbleibenalle.stressi.domain.observer.DefaultObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.domain.usecase.UseCase;
import org.wirbleibenalle.stressi.ui.base.Presenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by and on 26.10.16.
 */

public class MainPresenter extends Presenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private GetEventsUseCase getEventsUseCase;
    private LocalDate currentLocalDate;
    private Integer currentDay = new Integer(0);
    private int currentPosition;


    @Inject
    public MainPresenter(GetEventsUseCase getEventsUseCase) {
        this.getEventsUseCase = getEventsUseCase;
    }

    public void onSwitchDateByPosition(int position) {
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
        getEventsUseCase.setLocalDate(currentLocalDate);
        getEventsUseCase.setPosition(position);
        getEventsUseCase.execute(new LoadEventObserver(currentLocalDate,position));
    }

    public void loadEvents(final LocalDate localDate, final Integer day) {
        getEventsUseCase.setLocalDate(localDate);
        getEventsUseCase.setPosition(day);
        getEventsUseCase.unsubscribe();
        getEventsUseCase.execute(new LoadEventObserver(localDate,day));
    }

    public class LoadEventObserver extends DefaultObserver<List<EventItem>>{
        private final int day;
        private final LocalDate localDate;

        public LoadEventObserver(LocalDate localDate, int day) {
            this.day = day;
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
            view.hidePullToRefreshProgress(day);
            //TODO: implement error case
        }

        @Override
        public void onNext(List<EventItem> eventItemList) {
            super.onNext(eventItemList);
            Log.d(TAG, "onNext : day" + day);
            view.setItemsToRecycleView(eventItemList);
            view.hidePullToRefreshProgress(day);
            view.setDateToTitle(localDate.toString());
        }
    }
    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        view.initializeRecyclerView();
//        loadEvents(initLocalDate,currentDay);
    }
}
