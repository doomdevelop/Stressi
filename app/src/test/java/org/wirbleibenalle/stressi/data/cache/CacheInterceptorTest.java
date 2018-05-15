package org.wirbleibenalle.stressi.data.cache;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PowerMockListener(AnnotationEnabler.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({Request.class, Interceptor.Chain.class, Response.class, okhttp3.HttpUrl.class, okhttp3.ResponseBody.class, Response.Builder.class, okhttp3.MediaType.class,})
public class CacheInterceptorTest {

    @Mock
    CacheInterceptor cacheInterceptor;
    @Mock
    CacheController<CacheEvent> cacheController;
    @Mock
    CacheEvent cacheEvent;

    private okhttp3.HttpUrl url = PowerMockito.mock(okhttp3.HttpUrl.class);
    private Request request = PowerMockito.mock(Request.class);
    private Interceptor.Chain chain = PowerMockito.mock(Interceptor.Chain.class);
    private Response response = PowerMockito.mock(Response.class);
    private Response response2 = PowerMockito.mock(Response.class);
    private okhttp3.ResponseBody responseBody = PowerMockito.mock(okhttp3.ResponseBody.class);
    private okhttp3.ResponseBody responseBody2 = PowerMockito.mock(okhttp3.ResponseBody.class);
    private Response.Builder responseBuilder = PowerMockito.mock(Response.Builder.class);
    private okhttp3.MediaType mediaType = PowerMockito.mock(okhttp3.MediaType.class);

    private static final String CORRECT_DATE = "2018-02-19";
    private static final String CORRECT_URL_WITH_DATE = "termine.php?day=" + CORRECT_DATE;
    private static final String WRONG_URL_WITH_DATE = "termine.php?day=2018/02/19";
    private static final String[] WRONG_DATE_FORMATS = {"2018/02/19", "2018/Jan/09", "2018-jan-19", "01-12-2018"};
    private static final String DUMMY_HTML = "<div class=\"ueberschrift\">Terminator</div>";
    private static final String[] SPLIT_CORRECT_URL = CORRECT_URL_WITH_DATE.split("=");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ResponseBody.class);
        cacheInterceptor = spy(new CacheInterceptor(cacheController));
    }

    @Test
    public void intercept_should_neverCallCacheController_When_urlNull() throws IOException {
        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(response);
        when(request.url()).thenReturn(null);
        Response responseReturn = cacheInterceptor.intercept(chain);

        verify(cacheController, never()).getLastCache(any(String.class));
        verify(cacheController, never()).cache(ArgumentMatchers.<CacheEvent>any());
        verify(cacheController, never()).setOnPullToRefresh(SPLIT_CORRECT_URL[1], false);
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
        verify(cacheController, never()).cache(ArgumentMatchers.<CacheEvent>any());
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
        verify(cacheController, never()).cache(ArgumentMatchers.<CacheEvent>any());
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

    private void prepareResponseBuilderMocks() throws IOException {
        when(responseBody.contentType()).thenReturn(mediaType);
        when(ResponseBody.create(mediaType, responseBody.string())).thenReturn(responseBody2);

        when(response.newBuilder()).thenReturn(responseBuilder);
        when(responseBuilder.body(responseBody2)).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(response2);
    }

    private void prepareRequestWithCorrectUrl() throws IOException {
        when(chain.proceed(request)).thenReturn(response);
        when(request.url()).thenReturn(url);
        when(url.toString()).thenReturn(CORRECT_URL_WITH_DATE);
    }

    @Test
    public void intercept_should_NotReturnCache_when_userMadePushToRefresh() throws IOException {
        when(chain.request()).thenReturn(request);

        prepareRequestWithCorrectUrl();

        when(cacheController.isOnPullToRefresh(SPLIT_CORRECT_URL[1])).thenReturn(true);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(DUMMY_HTML);

        prepareResponseBuilderMocks();
        Response newResponse = cacheInterceptor.intercept(chain);

        Assertions.assertThat(newResponse).isNotNull();
        verify(cacheController, never()).getLastCache(SPLIT_CORRECT_URL[1]);
        verify(cacheController).cache(ArgumentMatchers.<CacheEvent>any());
    }

    @Test
    public void intercept_should_setBackPushToRefreshFalse_when_userMadePushToRefresh() throws IOException {
        when(chain.request()).thenReturn(request);
        prepareRequestWithCorrectUrl();
        when(cacheController.isOnPullToRefresh(SPLIT_CORRECT_URL[1])).thenReturn(true);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(DUMMY_HTML);

        prepareResponseBuilderMocks();

        Response newResponse = cacheInterceptor.intercept(chain);

        Assertions.assertThat(newResponse).isNotNull();
        verify(cacheController, never()).getLastCache(SPLIT_CORRECT_URL[1]);
        verify(cacheController).setOnPullToRefresh(SPLIT_CORRECT_URL[1], false);
    }
}