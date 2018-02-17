package org.wirbleibenalle.stressi.data.cache;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.extension.listener.AnnotationEnabler;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockListener;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wirbleibenalle.stressi.data.model.CacheEvent;
import org.wirbleibenalle.stressi.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PowerMockListener(AnnotationEnabler.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({Request.class, Interceptor.Chain.class, Response.class, okhttp3.HttpUrl.class})
public class CacheInterceptorTest {

    @Mock
    CacheInterceptor cacheInterceptor;
    @Mock
    CacheController<CacheEvent> cacheController;
    @Mock
    CacheEvent cacheEvent;

    okhttp3.HttpUrl url = PowerMockito.mock(okhttp3.HttpUrl.class);

    Request request = PowerMockito.mock(Request.class);
    Interceptor.Chain chain = PowerMockito.mock(Interceptor.Chain.class);
    Response response = PowerMockito.mock(Response.class);
    private static final String CORRECT_DATE = "2018-02-19";
    private static final String CORRECT_URL_WITH_DATE = "termine.php?day=" + CORRECT_DATE;
    private static final String WRONG_URL_WITH_DATE = "termine.php?day=2018/02/19";
    private static final String[] WRONG_DATE_FORMATS = {"2018/02/19", "2018/Jan/09", "2018-jan-19", "01-12-2018"};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cacheInterceptor = spy(new CacheInterceptor(cacheController));
    }

    @Test
    public void intercept_should_neverCallCacheController_When_urlNull() throws IOException {
        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(response);
        when(request.url()).thenReturn(null);
        Response responseReturn = cacheInterceptor.intercept(chain);

        verify(cacheController, never()).getLastCache(any(String.class));
        verify(cacheController, never()).cache(Matchers.<CacheEvent>any());
        assertEquals(response, responseReturn);
    }

    @Test
    public void intercept_should_neverCallCacheController_When_urlContainNoDateAsQuery() throws IOException {
        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(response);
        when(request.url()).thenReturn(url);
        when(url.toString()).thenReturn(Constants.BASE_URL);
        Response responseReturn = cacheInterceptor.intercept(chain);

        verify(cacheController, never()).getLastCache(any(String.class));
        verify(cacheController, never()).cache(Matchers.<CacheEvent>any());
        assertEquals(response, responseReturn);
    }

    @Test
    public void intercept_should_neverCallCacheController_When_urlContainWrongFormatedDateAsQuery() throws IOException {
        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(response);
        when(request.url()).thenReturn(url);
        when(url.toString()).thenReturn(Constants.BASE_URL + WRONG_URL_WITH_DATE);
        Response responseReturn = cacheInterceptor.intercept(chain);

        verify(cacheController, never()).getLastCache(any(String.class));
        verify(cacheController, never()).cache(Matchers.<CacheEvent>any());
        assertEquals(response, responseReturn);
    }

    @Test
    public void isDateValid_Should_AllowOnlyOneFormat() {
        String[] urlSplited = new String[2];
        urlSplited[0] = Constants.BASE_URL;

        for (String wrongDate : WRONG_DATE_FORMATS) {
            urlSplited[1] = wrongDate;
            Assertions.assertThat(cacheInterceptor.isDateValid(urlSplited)).isFalse();
        }
        urlSplited[1] = CORRECT_DATE;
        Assertions.assertThat(cacheInterceptor.isDateValid(urlSplited)).isTrue();
    }
}