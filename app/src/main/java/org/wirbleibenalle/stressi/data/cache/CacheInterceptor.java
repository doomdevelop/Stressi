package org.wirbleibenalle.stressi.data.cache;

import android.support.annotation.VisibleForTesting;

import org.joda.time.LocalDate;
import org.wirbleibenalle.stressi.data.model.CacheEvent;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CacheInterceptor implements Interceptor {
    private final CacheController<CacheEvent> cacheController;

    @Inject
    public CacheInterceptor(CacheController<CacheEvent> cacheController) {
        this.cacheController = cacheController;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        Request request = chain.request();
        if(request.url() == null){
            return chain.proceed(request);
        }
        final String url = request.url().toString();
        String[] splitUrl = null;
        String date = null;

        if (url != null) {
            splitUrl = url.split("=");
            if (isDateValid(splitUrl)) {
                date = splitUrl[1];
            }else {
                return chain.proceed(request);
            }
        }

        boolean isOnPushToRefrash = cacheController.isOnPullToRefresh(date);
        if (cacheController.canUseCache() && !isOnPushToRefrash && date != null) {
            CacheEvent cacheEvent = cacheController.getLastCache(date);
            if (cacheEvent != null) {
                String responseString = cacheEvent.htmlResponse;
                return new Response.Builder()
                        .code(200)
                        .message(responseString)
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/xhtml"), responseString.getBytes()))
                        .addHeader("content-type", "text/html")
                        .build();
            }
        }
        response = chain.proceed(request);
        String htmlResponse = response.body().string();
        if (date != null) {
            cacheController.cache(new CacheEvent(htmlResponse, date, System.currentTimeMillis()));
            if(isOnPushToRefrash) {
                cacheController.setOnPullToRefresh(date, false);
            }
        }
        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), htmlResponse)).build();
    }

    private LocalDate parse(String[] splitedUrl) {
        if (splitedUrl.length != 2) {
            return null;
        }
        String dateStr = splitedUrl[1];
        LocalDate localDate = new LocalDate(dateStr);
        return localDate;
    }

    @VisibleForTesting
    boolean isDateValid(String[] splitedUrl) {
        if (splitedUrl.length == 2 && splitedUrl[1] != null && splitedUrl[1].length() > 0) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            try {
                format.parse(splitedUrl[1]);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
