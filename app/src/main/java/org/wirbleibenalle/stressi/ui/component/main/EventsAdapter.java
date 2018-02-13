package org.wirbleibenalle.stressi.ui.component.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wirbleibenalle.stressi.data.model.Event;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.ArrayList;
import java.util.List;

import static org.wirbleibenalle.stressi.ui.component.main.EventItemViewHolder.*;

/**
 * Created by and on 27.10.16.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EventItem> eventItemList;
    private final RecyclerItemListener onRowClickListener;
    private final EventItemViewHolderListener eventItemViewHolderListener;

    private Integer currentDay;

    public EventsAdapter(RecyclerItemListener onRowClickListener, EventItemViewHolderListener eventItemViewHolderListener) {
        this.onRowClickListener = onRowClickListener;
        this.eventItemViewHolderListener = eventItemViewHolderListener;
        this.eventItemList = new ArrayList<>();
    }

    public void setItems(List<EventItem> eventItemList) {
        this.eventItemList = eventItemList;
        notifyDataSetChanged();
    }

    public List<EventItem> getEventItemList() {
        return eventItemList;
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventItemViewHolder(view, onRowClickListener, eventItemViewHolderListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventItemViewHolder eventItemViewHolder = (EventItemViewHolder) holder;
        EventItem eventItem = eventItemList.get(position);
        eventItemViewHolder.render(position, eventItem);
    }

    @Override
    public int getItemCount() {
        return eventItemList.size();
    }
}
