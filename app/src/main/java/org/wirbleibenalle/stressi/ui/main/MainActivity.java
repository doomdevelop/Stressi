package org.wirbleibenalle.stressi.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.StressiApplication;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.base.BaseActivity;
import org.wirbleibenalle.stressi.ui.component.pageView.CustomPagerAdapter;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements MainView {
    @Inject
    MainPresenter presenter;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private static final String TAG = CustomPagerAdapter.class.getSimpleName();

    private CustomPagerAdapter customPagerAdapter;
    @Override
    protected void initializeDagger() {
        StressiApplication app = (StressiApplication) getApplication();
        app.getMainComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        presenter.onResumed();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void start() {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void initializeRecyclerView() {
        customPagerAdapter = new CustomPagerAdapter(this, LocalDate.now(), pageAdapterCallback);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //positionOffset by page change from 0.0-1.0
                Log.d(TAG, "onPageScrolled() position " + position+" positionOffset: " +positionOffset+" positionOffsetPixels "+positionOffsetPixels);


            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected() " + position);
                presenter.onSwitchDateByPosition(position);

//                setDateToTitle(newDate.toString());
//                presenter.loadEvents(newDate, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageSelected() state " + state);

            }
        });
    }

    @Override
    public void setItemsToRecycleView(List<EventItem> eventItemList) {
        customPagerAdapter.setItemsToRecycleView(eventItemList);
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
        setTitle(title);
    }

    private CustomPagerAdapter.PageAdapterCallback pageAdapterCallback = new CustomPagerAdapter.PageAdapterCallback() {
        @Override
        public void onListItemclicked(int position) {

        }

        @Override
        public void onPullToRefresh(LocalDate localDate, Integer day) {
            presenter.loadEvents(localDate, day);
        }

        @Override
        public void loadEvents(LocalDate localDate, Integer day) {
            presenter.loadEvents(localDate, day);
        }
    };
}
