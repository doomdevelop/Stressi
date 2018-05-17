package org.wirbleibenalle.stressi.data.remote.handler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NetworkConnectionHandler extends ConnectionHandler {
    private final ConnectivityManager connectivityManager;
    private List<ConnectionChangeListener> connectionChangeListenerList = new ArrayList<>();

    @Inject
    public void addResponseErrorListener(ConnectionChangeListener connectionChangeListener) {
        connectionChangeListenerList.add(connectionChangeListener);
    }

    public void removeResponseErrorListener(ConnectionChangeListener connectionChangeListener) {
        connectionChangeListenerList.remove(connectionChangeListener);
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
