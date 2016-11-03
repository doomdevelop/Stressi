package org.wirbleibenalle.stressi.ui.main;

import org.wirbleibenalle.stressi.ui.base.Presenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

/**
 * Created by and on 26.10.16.
 */

public interface MainView extends Presenter.View {
    void initializeRecyclerView();

    void setItemsToRecycleView(List<EventItem> eventItemList);

    void hidePullToRefreshProgress();

    void showPullToRefreshProgress();
}
