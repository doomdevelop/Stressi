package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.util.Log;

import org.wirbleibenalle.stressi.data.repository.DataRepository;
import org.wirbleibenalle.stressi.domain.observer.DefaultObserver;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.domain.usecase.UseCase;
import org.wirbleibenalle.stressi.ui.base.Presenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by and on 26.10.16.
 */

public class MainPresenter extends Presenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private DataRepository dataRepository;


    @Inject
    public MainPresenter(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    void onListItemclicked(int position) {

    }

    void onPullToRefresh() {
        loadEvents();
    }

    public void loadEvents() {
        UseCase useCase = new GetEventsUseCase(dataRepository, -1L);
        useCase.execute(new DefaultObserver<List<EventItem>>() {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.d(TAG, e.getMessage());
                view.hidePullToRefreshProgress();
            }

            @Override
            public void onNext(List<EventItem> eventItemList) {
                super.onNext(eventItemList);
                Log.d(TAG, eventItemList.get(0).toString());
                view.setItemsToRecycleView(eventItemList);
                view.hidePullToRefreshProgress();
            }
        });
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        view.initializeRecyclerView();
        loadEvents();
    }
}
