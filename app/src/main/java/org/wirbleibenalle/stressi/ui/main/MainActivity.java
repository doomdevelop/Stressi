package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.wirbleibenalle.stressi.StressiApplication;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.base.BaseActivity;
import org.wirbleibenalle.stressi.ui.component.main.EventsAdapter;
import org.wirbleibenalle.stressi.ui.component.main.SimpleDividerItemDecoration;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements MainView {
    @Inject
    MainPresenter presenter;
    @Bind(R.id.refresh_events_list)
    SwipeRefreshLayout refreshEventList;
    @Bind(R.id.rv_events_list)
    RecyclerView rvEventsList;
    EventsAdapter eventsAdapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void initializeDagger() {
        StressiApplication app = (StressiApplication) getApplication();
        app.getMainComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        presenter.onResumed();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void start() {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void initializeRecyclerView() {
        eventsAdapter = new EventsAdapter(position -> presenter.onListItemclicked(position));
        refreshEventList.setOnRefreshListener(() -> presenter.onPullToRefresh());
        layoutManager = new LinearLayoutManager(this);
        rvEventsList.setLayoutManager(layoutManager);
        rvEventsList.setHasFixedSize(false);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(
            rvEventsList.getContext());
        rvEventsList.addItemDecoration(dividerItemDecoration);
        rvEventsList.setAdapter(eventsAdapter);
    }

    @Override
    public void setItemsToRecycleView(List<EventItem> eventItemList) {
        eventsAdapter.setItems(eventItemList);
    }

    @Override
    public void hidePullToRefreshProgress() {
        refreshEventList.setRefreshing(false);
    }
}
