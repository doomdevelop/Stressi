package org.wirbleibenalle.stressi.data.remote;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorHandlerInterceptor implements Interceptor {
    private final ConnectivityManager connectivityManager;
    private List<ResponseErrorListener> responseErrorListenerList = new ArrayList<>();

    @Inject
    public ErrorHandlerInterceptor(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public void addResponseErrorListener(ResponseErrorListener responseErrorListener) {
        responseErrorListenerList.add(responseErrorListener);
    }

    public void removeResponseErrorListener(ResponseErrorListener responseErrorListener) {
        responseErrorListenerList.remove(responseErrorListener);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWiFiConnected = networkInfo != null ? networkInfo.isConnected() : false;
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo != null ? networkInfo.isConnected() : false;

        int errorType = ResponseError.ERROR_UNDEFINED;
        if (!isWiFiConnected && !isMobileConn) {
            errorType = ResponseError.ERROR_NETWORK_CONNECTION;
            provideErrorToListeners(new ResponseError(errorType, -1));
        }
        okhttp3.Response response = chain.proceed(request);

        provideErrorToListeners(new ResponseError(errorType, response.code()));
        return response;
    }

    private void provideErrorToListeners(ResponseError responseError) {
        for (ResponseErrorListener responseErrorListener : responseErrorListenerList) {
            responseErrorListener.onResponseError(responseError);
        }
    }
}
