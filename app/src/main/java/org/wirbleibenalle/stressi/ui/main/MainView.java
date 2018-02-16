package org.wirbleibenalle.stressi.ui.main;

import org.joda.time.DateTime;
import org.wirbleibenalle.stressi.ui.base.Presenter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

/**
 * Created by and on 26.10.16.
 */

public interface MainView extends Presenter.View {
    void initializeViewComponents(int position);

    void setItemsToRecycleView(List<EventItem> events, int position);

    void hidePullToRefreshProgress(int day);

    void showPullToRefreshProgress(int day);

    void setDateToTitle(String title);

    void showNoConnectionErrorMessage();

    void addEventToCalendar(EventItem eventItem, DateTime datetime, String shortTitle);

    void showEventOnMap(String address);

    void shareEvent(String Subject, String text);
}