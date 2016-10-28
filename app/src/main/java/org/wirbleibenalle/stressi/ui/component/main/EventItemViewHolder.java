package org.wirbleibenalle.stressi.ui.component.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
    @Bind(R.id.event_description)
    TextView tvDescription;
    @Bind(R.id.event_title)
    TextView tvTitle;

    public EventItemViewHolder(View itemView, RecyclerItemListener onRowClickListener) {
        super(itemView);
        this.onRowClickListener = onRowClickListener;
        ButterKnife.bind(this, itemView);
    }

    public void render(int position, EventItem eventItem) {
        tvTime.setText(eventItem.getPubDate());
        tvDescription.setText(eventItem.getDescription());
        tvTitle.setText(eventItem.getTitle());
//        viewInspectionSummary.setBackgroundResource(R.drawable.bg_inspection_item);
//        viewInspectionSummary.renderInspectionSummary(eventItem);
        itemView.setOnClickListener(v -> onRowClickListener.onItemSelected(position));
    }
}
