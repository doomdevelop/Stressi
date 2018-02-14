package org.wirbleibenalle.stressi.data.remote;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorHandlerInterceptor implements Interceptor {
    private final ConnectivityManager connectivityManager;

    @Inject
    public ErrorHandlerInterceptor(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWiFiConnected = networkInfo != null ? networkInfo.isConnected() : false;
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo != null ? networkInfo.isConnected() : false;

        int errorType = ResponseError.ERROR_UNDEFINED;
        if(!isWiFiConnected && !isMobileConn){
            errorType = ResponseError.ERROR_NETWORK_CONNECTION;
            throw new ResponseException(new ResponseError(errorType,-1));
        }
        okhttp3.Response response = chain.proceed(request);
        if(response.code() == 200){
            return response;
        }
        throw new ResponseException(new ResponseError(errorType,response.code()));
    }
}
