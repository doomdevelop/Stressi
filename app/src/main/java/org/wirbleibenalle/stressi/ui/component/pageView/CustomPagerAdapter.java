package org.wirbleibenalle.stressi.ui.component.pageView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.component.main.EventsAdapter;
import org.wirbleibenalle.stressi.ui.component.main.SimpleDividerItemDecoration;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by and on 02.11.16.
 */

public class CustomPagerAdapter extends PagerAdapter {
    private String pageTitle;
    private final Context context;
    private final PageAdapterCallback pageAdapterCallback;
    @Bind(R.id.refresh_events_list)
    SwipeRefreshLayout refreshEventList;
    @Bind(R.id.rv_events_list)
    RecyclerView rvEventsList;
    EventsAdapter eventsAdapter = null;
    LinearLayoutManager layoutManager;
    private static final String TAG = CustomPagerAdapter.class.getSimpleName();

    public CustomPagerAdapter(Context context, LocalDate localDate, PageAdapterCallback
        pageAdapterCallback) {
        Log.d(TAG, "constructor CustomPagerAdapter()");
        setPageTitle(localDate);
        this.context = context;
        this.pageAdapterCallback = pageAdapterCallback;

    }

    private void setPageTitle(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E-MMM-yy");
        this.pageTitle = localDate.toString(formatter);
    }

    public void setItemsToRecycleView(List<EventItem> eventItemList) {
        if (eventItemList == null || eventItemList.size() <= 0) {
            return;
        }
        Log.d(TAG, "setItemsToRecycleView() currentDay: " + eventsAdapter.getCurrentDay() + " eventItemList.get(0)" +
            ".getDay" +
            "(): " + "" + eventItemList.get(0).getDay().intValue());
//        if (eventsAdapter.getCurrentDay().intValue() == eventItemList.get(0).getDay().intValue()) {
        eventsAdapter.setItems(eventItemList);
        notifyDataSetChanged();
//        }
    }

    public void showPullToRefreshProgress() {
        refreshEventList.setRefreshing(true);
    }

    public void hidePullToRefreshProgress() {
        refreshEventList.setRefreshing(false);
    }

    private void initializeRecyclerView(Integer currentDay) {
        eventsAdapter = new EventsAdapter(position -> pageAdapterCallback.onListItemclicked(position));
        eventsAdapter.setCurrentDay(currentDay);
        refreshEventList.setOnRefreshListener(() -> pageAdapterCallback.onPullToRefresh
            (LocalDate.now().plusDays(currentDay), currentDay));
        layoutManager = new LinearLayoutManager(context);
        rvEventsList.setLayoutManager(layoutManager);
        rvEventsList.setHasFixedSize(false);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(
            rvEventsList.getContext());
        rvEventsList.addItemDecoration(dividerItemDecoration);
        rvEventsList.setAdapter(eventsAdapter);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Log.d(TAG, "instantiateItem position : " + position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.event_view_page, collection, false);
        collection.addView(layout);
        ButterKnife.bind(this, layout);
//        pageAdapterCallback.loadEvents(LocalDate.now().plusDays(position), position);
        initializeRecyclerView(position);
        return layout;
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle;
    }

    public interface PageAdapterCallback {
        void onListItemclicked(int position);

        void onPullToRefresh(LocalDate localDate, Integer day);

        void loadEvents(LocalDate localDate, Integer day);
    }
}
