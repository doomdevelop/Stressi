package org.wirbleibenalle.stressi.ui.component.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wirbleibenalle.stressi.data.model.Event;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by and on 27.10.16.
 */

public class EventItemViewHolder extends RecyclerView.ViewHolder {

    private RecyclerItemListener onRowClickListener;

    @Bind(R.id.event_time)
    TextView tvTime;
    @Bind(R.id.event_address)
    TextView tvAddress;
    @Bind(R.id.event_place)
    TextView tvPlace;
    @Bind(R.id.event_description)
    TextView tvDescription;
    @Bind(R.id.event_share)
    ImageView ivShare;

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

        ivShare.setOnClickListener(v -> eventItemViewHolderListener.onShareClicked(eventItem));
        itemView.setOnClickListener(v -> onRowClickListener.onItemSelected(position));
    }

    public interface EventItemViewHolderListener {
        void onShareClicked(EventItem eventItem );

        void showOnGoogleMap(EventItem eventItem );
    }
}
