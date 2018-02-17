package org.wirbleibenalle.stressi.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.joda.time.DateTime;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
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
                Log.i(TAG, "onPageScrolled() position " + position + " positionOffset: " + positionOffset + " positionOffsetPixels " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected() " + position);
                animateTitle();
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

    @Override
    public void showNoConnectionErrorMessage() {
        super.showError(getString(R.string.no_connection));
    }

    @Override
    public void addEventToCalendar(EventItem eventItem, DateTime datetime, String shortTitle) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, datetime.getMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, datetime.getMillis() + 60 * 60 * 1000);
        intent.putExtra(CalendarContract.Events.TITLE, shortTitle);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventItem.getDescription());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventItem.getAddress());
        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showEventOnMap(String address) {
        Uri gmmIntentUri = Uri.parse(address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void shareEvent(String subject, String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        if (sharingIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    private CustomPagerAdapter.PageAdapterCallback pageAdapterCallback = new CustomPagerAdapter.PageAdapterCallback() {
        @Override
        public void onListItemclicked(int position) {

        }

        @Override
        public void onPullToRefresh() {
            presenter.onPullToRefresh();
        }
    };

    @Override
    public void onShareEventClicked(EventItem eventItem) {
        presenter.onShareEvent(eventItem);
    }

    @Override
    public void onShowEventOnMapClicked(EventItem eventItem) {
        presenter.onShowEventOnMap(eventItem);
    }

    @Override
    public void onAddEventToCalendarClicked(EventItem eventItem) {
        presenter.onAddEventToCalendar(eventItem);
    }
}