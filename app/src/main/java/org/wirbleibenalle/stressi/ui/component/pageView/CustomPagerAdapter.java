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
import org.wirbleibenalle.stressi.data.model.Events;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.component.main.EventItemViewHolder.EventItemViewHolderListener;
import org.wirbleibenalle.stressi.ui.component.main.EventsAdapter;
import org.wirbleibenalle.stressi.ui.component.main.SimpleDividerItemDecoration;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by and on 02.11.16.
 */

public class CustomPagerAdapter extends PagerAdapter {
    private String pageTitle;
    private final Context context;
    private final PageAdapterCallback pageAdapterCallback;
    private final EventItemViewHolderListener eventItemViewHolderListener;
    private SwipeRefreshLayout refreshEventList;
    private RecyclerView rvEventsList;
    private EventsAdapter eventsAdapter = null;
    private LinearLayoutManager layoutManager;
    private Map<Integer, ViewHolder> viewHolderMap = new HashMap<>();
    private static final String TAG = CustomPagerAdapter.class.getSimpleName();

    public CustomPagerAdapter(Context context, LocalDate localDate, PageAdapterCallback
            pageAdapterCallback, EventItemViewHolderListener eventItemViewHolderListener) {
        Log.d(TAG, "constructor CustomPagerAdapter()");
        setPageTitle(localDate);
        this.context = context;
        this.pageAdapterCallback = pageAdapterCallback;
        this.eventItemViewHolderListener = eventItemViewHolderListener;
    }

    private void setPageTitle(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E-MMM-yy");
        this.pageTitle = localDate.toString(formatter);
    }

    public void setItemsToRecycleView(List<EventItem> events, int position) {
        if (events == null || events == null || events.size() <= 0) {
            return;
        }
        Log.d(TAG, "setItemsToRecycleView() currentDay: " + eventsAdapter.getCurrentDay() +
                " eventItemList.get(0)");
        viewHolderMap.get(position).eventsAdapter.setItems(events);
        notifyDataSetChanged();
    }

    public boolean containItemsInRecycleView(int position){
        if(viewHolderMap.containsKey(position) && viewHolderMap.get(position).eventsAdapter.getEventItemList().size()>0){
//            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void showPullToRefreshProgress(int position) {
        viewHolderMap.get(position).refreshEventList.setRefreshing(true);
    }

    public void hidePullToRefreshProgress(int position) {
        viewHolderMap.get(position).refreshEventList.setRefreshing(false);
    }

    private void initializeRecyclerView(Integer currentDay) {
        eventsAdapter = new EventsAdapter(position -> pageAdapterCallback.onListItemclicked(position), eventItemViewHolderListener);
        eventsAdapter.setCurrentDay(currentDay);
        refreshEventList.setOnRefreshListener(() -> pageAdapterCallback.onPullToRefresh());
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
        Log.d(TAG, "instantiateItem position : " + position + " collection.getChildCount(): " + collection.getChildCount());
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.event_view_page, collection, false);
        refreshEventList = (SwipeRefreshLayout) layout.findViewById(R.id.refresh_events_list);
        rvEventsList = (RecyclerView) layout.findViewById(R.id.rv_events_list);
        initializeRecyclerView(position);
        if (viewHolderMap.size() == 0) {
            refreshEventList.setRefreshing(true);
        }
        viewHolderMap.put(position, new ViewHolder(refreshEventList, rvEventsList, eventsAdapter));
        collection.addView(layout);
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

    private class ViewHolder {
        public final SwipeRefreshLayout refreshEventList;
        public final RecyclerView rvEventsList;
        public final EventsAdapter eventsAdapter;

        public ViewHolder(SwipeRefreshLayout refreshEventList, RecyclerView rvEventsList, EventsAdapter eventsAdapter) {
            this.refreshEventList = refreshEventList;
            this.rvEventsList = rvEventsList;
            this.eventsAdapter = eventsAdapter;
        }

    }

    public interface PageAdapterCallback {
        void onListItemclicked(int position);

        void onPullToRefresh();
    }
}
