package org.wirbleibenalle.stressi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.StressiApplication;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.base.BaseActivity;
import org.wirbleibenalle.stressi.ui.component.main.EventItemViewHolder;
import org.wirbleibenalle.stressi.ui.component.pageView.CustomPagerAdapter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements MainView, EventItemViewHolder.EventItemViewHolderListener {

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
    public void showError(String errorMessage) {

    }

    @Override
    public void initializeViewComponents(int position) {
        customPagerAdapter = new CustomPagerAdapter(this, LocalDate.now(), pageAdapterCallback, this);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //positionOffset by page change from 0.0-1.0
                Log.d(TAG, "onPageScrolled() position " + position + " positionOffset: " + positionOffset + " positionOffsetPixels " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected() " + position);
                presenter.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageSelected() state " + state);
            }
        });
    }

    @Override
    public void setItemsToRecycleView(List<EventItem> events, int position) {
        customPagerAdapter.setItemsToRecycleView(events, position);
        Log.d("MainActivity", "size events: " + events.size());
    }

    @Override
    public void hidePullToRefreshProgress(int day) {
        customPagerAdapter.hidePullToRefreshProgress(day);
    }

    @Override
    public void showPullToRefreshProgress(int day) {
        customPagerAdapter.showPullToRefreshProgress(day);
    }

    @Override
    public void setDateToTitle(String title) {
        setTitle(title);
    }

    private CustomPagerAdapter.PageAdapterCallback pageAdapterCallback = new CustomPagerAdapter.PageAdapterCallback() {
        @Override
        public void onListItemclicked(int position) {

        }

        @Override
        public void onPullToRefresh() {
            presenter.loadEvents();
        }
    };

    @Override
    public void onShareClicked(EventItem eventItem) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = eventItem.getTime() + " | " + eventItem.getPlace() + " | " + eventItem.getAddress();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\n" + eventItem.getDescription());
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void showOnGoogleMap(EventItem eventItem) {

    }
}