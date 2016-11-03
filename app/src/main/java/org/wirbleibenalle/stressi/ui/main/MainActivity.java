package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.StressiApplication;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.base.BaseActivity;
import org.wirbleibenalle.stressi.ui.component.pageView.CustomPagerAdapter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements MainView {
    @Inject
    MainPresenter presenter;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private static final String TAG = CustomPagerAdapter.class.getSimpleName();

    private CustomPagerAdapter customPagerAdapter;
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
        this.customPagerAdapter = new CustomPagerAdapter(this, LocalDate.now(), pageAdapterCallback);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected() " + position);
                presenter.loadEvents(LocalDate.now().plusDays(position), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        eventsAdapter = new EventsAdapter(position -> presenter.onListItemclicked(position));
//        refreshEventList.setOnRefreshListener(() -> presenter.onPullToRefresh());
//        layoutManager = new LinearLayoutManager(this);
//        rvEventsList.setLayoutManager(layoutManager);
//        rvEventsList.setHasFixedSize(false);
//        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(
//            rvEventsList.getContext());
//        rvEventsList.addItemDecoration(dividerItemDecoration);
//        rvEventsList.setAdapter(eventsAdapter);
    }

    @Override
    public void setItemsToRecycleView(List<EventItem> eventItemList) {
        customPagerAdapter.setItemsToRecycleView(eventItemList);
    }

    @Override
    public void hidePullToRefreshProgress() {
        customPagerAdapter.hidePullToRefreshProgress();
    }

    @Override
    public void showPullToRefreshProgress() {
        customPagerAdapter.showPullToRefreshProgress();
    }

    private CustomPagerAdapter.PageAdapterCallback pageAdapterCallback = new CustomPagerAdapter.PageAdapterCallback() {
        @Override
        public void onListItemclicked(int position) {

        }

        @Override
        public void onPullToRefresh(LocalDate localDate, Integer day) {
            presenter.loadEvents(localDate, day);
        }

        @Override
        public void loadEvents(LocalDate localDate, Integer day) {
            presenter.loadEvents(localDate, day);
        }
    };
}
