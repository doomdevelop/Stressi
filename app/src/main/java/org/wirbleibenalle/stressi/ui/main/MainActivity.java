package org.wirbleibenalle.stressi.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.animation.BounceInterpolator;
import org.wirbleibenalle.stressi.ui.base.BaseActivity;
import org.wirbleibenalle.stressi.ui.base.StressiViewModelFactory;
import org.wirbleibenalle.stressi.ui.component.main.EventItemViewHolder;
import org.wirbleibenalle.stressi.ui.component.pageView.CustomPagerAdapter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity<MainActivityContract.View, MainActivityContract.Presenter>
    implements MainActivityContract.View, EventItemViewHolder.EventItemViewHolderListener {

    private MainPresenter presenter;

    @Inject
    StressiViewModelFactory viewModelFactory;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_toolbar_title)
    protected TextView tvTitle;

    private CustomPagerAdapter customPagerAdapter;

    @Override
    protected MainPresenter initPresenter() {
        presenter = ViewModelProviders.of(this, viewModelFactory).get(MainPresenter.class);
        if (!presenter.getStateBundle().getBoolean(MainPresenter.class.getSimpleName(), false)) {
            presenter.getStateBundle().putBoolean(MainPresenter.class.getSimpleName(), true);
            presenter.onPresenterCreated();
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void showError(String errorMessage) {
        super.showError(errorMessage);
    }

    @Override
    public void initializeViewComponents(int position, LocalDate currentDate) {
        customPagerAdapter = new CustomPagerAdapter(this, currentDate, pageAdapterCallback, this);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //positionOffset by page change from 0.0-1.0
                Timber.i("onPageScrolled() position " + position + " positionOffset: " + positionOffset + " positionOffsetPixels " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Timber.i("onPageSelected() %s", position);
                animateTitle();
                presenter.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Timber.d("onPageSelected() state %s", state);
            }
        });
    }

    @Override
    public void setItemsToRecycleView(@NonNull List<EventItem> events, int position) {
        customPagerAdapter.setItemsToRecycleView(events, position);
        Timber.d("size events: %s", events.size());
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
        checkNotNull(title, "date title can not be null");
        setTitle(title);
    }

    @Override
    public void showNoConnectionErrorMessage() {
        super.showError(getString(R.string.no_connection));
    }

    @Override
    public void addEventToCalendar(EventItem eventItem, DateTime datetime, String shortTitle) {
        checkNotNull(eventItem, "can not add eventItem as null to calendar");
        checkNotNull(datetime, "can not add datetime as null to calendar");
        checkNotNull(shortTitle, "can not add shortTitle as null to calendar");
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
        checkNotNull(address, "event address can not be null");
        Uri gmmIntentUri = Uri.parse(address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void shareEvent(@NonNull String subject, @NonNull String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        if (sharingIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    @Override
    public void showShareEventError() {
        showError(getString(R.string.shareEventError));
    }

    private final CustomPagerAdapter.PageAdapterCallback pageAdapterCallback = new CustomPagerAdapter.PageAdapterCallback() {
        @Override
        public void onListItemClicked(int position) {

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

    @Override
    protected Snackbar createSnackbar(String message) {
        return Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
    }

    @Override
    protected void initializeToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    protected void animateTitle() {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        tvTitle.startAnimation(myAnim);
    }
}