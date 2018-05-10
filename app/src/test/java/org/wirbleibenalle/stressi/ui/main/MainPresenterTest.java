package org.wirbleibenalle.stressi.ui.main;

import android.content.Context;
import android.content.res.Resources;

import net.danlew.android.joda.JodaTimeAndroid;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.wirbleibenalle.stressi.data.cache.EventCacheController;
import org.wirbleibenalle.stressi.data.remote.ErrorHandler;
import org.wirbleibenalle.stressi.data.repository.DataRepository;
import org.wirbleibenalle.stressi.domain.usecase.GetEventsUseCase;
import org.wirbleibenalle.stressi.ui.model.EventItem;

import java.io.InputStream;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by and on 24.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    @Mock
    DataRepository dataRepository;
    @Mock
    MainActivityContract.View mainView;

    @Mock
    MainPresenter.LoadEventObserver loadEventObserver;

    @Captor
    ArgumentCaptor<List<EventItem>> observerArgument;

    @Mock
    GetEventsUseCase mockGetEventsUseCase;
    @Mock
    EventCacheController eventCacheController;
    @Mock
    ErrorHandler errorHandler;
    MainPresenter mainPresenter;


    @Before
    public void setUp() throws Exception {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(resources.openRawResource(anyInt())).thenReturn(mock(InputStream.class));
        when(appContext.getResources()).thenReturn(resources);
        when(context.getApplicationContext()).thenReturn(appContext);
        JodaTimeAndroid.init(context);
        mainPresenter = new MainPresenter(mockGetEventsUseCase, eventCacheController, errorHandler);
        mainPresenter.attachView(mainView);
    }
}