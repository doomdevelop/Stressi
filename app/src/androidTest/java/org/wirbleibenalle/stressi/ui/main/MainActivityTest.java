package org.wirbleibenalle.stressi.ui.main;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.base.StressiViewModelFactory;

import static android.support.test.espresso.Espresso.onView;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    private MainPresenter mainPresenter;

    @org.junit.Before
    public void setUp() throws Exception {
        mainPresenter = mock(MainPresenter.class);
//        rule.getActivity().viewModelFactory = ViewModelUtil.createFor(viewModel);
        rule.getActivity().initializeViewComponents(0, LocalDate.now());
    }

    @org.junit.Test
    public void setItemsToRecycleView() {
//        onView(R.id.viewpager).check()
//        Espresso.onView(ViewMatchers.withId(R.id.))
    }



//    @NonNull
//    private RecyclerViewMatcher listMatcher() {
//        return new RecyclerViewMatcher(R.id.repo_list);
//    }
}