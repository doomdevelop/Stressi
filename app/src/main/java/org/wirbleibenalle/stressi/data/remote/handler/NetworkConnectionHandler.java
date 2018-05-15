package org.wirbleibenalle.stressi.data.remote.handler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.wirbleibenalle.stressi.data.remote.ResponseErrorListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NetworkConnectionHandler extends ConnectionHandler {
    private final ConnectivityManager connectivityManager;
    private List<ResponseErrorListener> responseErrorListenerList = new ArrayList<>();

    @Inject
    public void addResponseErrorListener(ResponseErrorListener responseErrorListener) {
        responseErrorListenerList.add(responseErrorListener);
    }

    public void removeResponseErrorListener(ResponseErrorListener responseErrorListener) {
        responseErrorListenerList.remove(responseErrorListener);
    }

    public NetworkConnectionHandler(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public boolean hasConnection() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWiFiConnected = networkInfo != null && networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo != null && networkInfo.isConnected();

        return isWiFiConnected || isMobileConn;
    }
}
