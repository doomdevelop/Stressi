package org.wirbleibenalle.stressi.data.remote.handler;

public interface ConnectionChangeListener {
    void onObtainConnection();
    void onLostConnection();
}
