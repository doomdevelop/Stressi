package org.wirbleibenalle.stressi.ui.main;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.ui.base.BaseContract;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

public class MainActivityContract {
    interface View extends BaseContract.View {
        void initializeViewComponents(int position, LocalDate currentDate);

        void setItemsToRecycleView(List<EventItem> events, int position);

        void showPageByPosition(int position);

        void hidePullToRefreshProgress(int day);

        void showPullToRefreshProgress(int day);

        void setDateToTitle(String title);

        void addEventToCalendar(EventItem eventItem, DateTime datetime, String shortTitle);

        void showEventOnMap(String address);

        void shareEvent(String Subject, String text);

        void showShareEventError();
    }

    interface Presenter extends BaseContract.Presenter<MainActivityContract.View> {
        void onPageSelected(int position);
    }
}
