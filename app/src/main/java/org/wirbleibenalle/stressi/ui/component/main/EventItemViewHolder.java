package org.wirbleibenalle.stressi.ui.component.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by and on 27.10.16.
 */

public class EventItemViewHolder extends RecyclerView.ViewHolder {

    private RecyclerItemListener onRowClickListener;

    @BindView(R.id.event_time)
    TextView tvTime;
    @BindView(R.id.event_address)
    TextView tvAddress;
    @BindView(R.id.event_place)
    TextView tvPlace;
    @BindView(R.id.event_description)
    TextView tvDescription;
    @BindView(R.id.event_share)
    ImageView ivShare;
    @BindView(R.id.event_show_map)
    ImageView ivShowMap;
    @BindView(R.id.event_calendar)
    ImageView ivCalendar;

    private final EventItemViewHolderListener eventItemViewHolderListener;

    public EventItemViewHolder(View itemView, RecyclerItemListener onRowClickListener, EventItemViewHolderListener eventItemViewHolderListener) {
        super(itemView);
        this.onRowClickListener = onRowClickListener;
        this.eventItemViewHolderListener = eventItemViewHolderListener;
        ButterKnife.bind(this, itemView);
    }

    public void render(int position, final EventItem eventItem) {
        tvTime.setText(eventItem.getTime());
        tvDescription.setText(eventItem.getDescription());
        tvPlace.setText(eventItem.getPlace());
        tvAddress.setText(eventItem.getAddress());

        ivShare.setOnClickListener(v -> eventItemViewHolderListener.onShareEventClicked(eventItem));
        ivShowMap.setOnClickListener(v-> eventItemViewHolderListener.onShowEventOnMapClicked(eventItem));
        ivCalendar.setOnClickListener(v->eventItemViewHolderListener.onAddEventToCalendarClicked(eventItem));
        itemView.setOnClickListener(v -> onRowClickListener.onItemSelected(position));
    }

    public interface EventItemViewHolderListener {
        void onShareEventClicked(EventItem eventItem );

        void onShowEventOnMapClicked(EventItem eventItem );

        void onAddEventToCalendarClicked(EventItem eventItem);
    }
}
