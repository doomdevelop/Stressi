package org.wirbleibenalle.stressi.domain.observer;

import android.annotation.SuppressLint;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Preconditions;

import org.wirbleibenalle.stressi.data.remote.handler.ConnectionHandler;
import org.wirbleibenalle.stressi.ui.base.BaseContract;

import io.reactivex.observers.DisposableObserver;

/**
 *
 * @param <T> Type of the parameter in the callback method Observable.onNext()
 * @param <CH> Type of {@link ConnectionHandler}
 * @param <V> Type of {@link BaseContract.View}
 */
public abstract class BaseObserver<T, CH extends ConnectionHandler, V extends BaseContract.View>
    extends DisposableObserver<T> {
    private final CH connectionHandler;

    @SuppressLint("RestrictedApi")
    public BaseObserver(CH connectionHandler) {
        Preconditions.checkNotNull(connectionHandler, "connectionHandler can not be null!");
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void onError(Throwable e) {
        if (!isViewAttached()) {
            return;
        }
        if (connectionHandler.hasConnection()) {
            getView().showError(e.getMessage());
        } else {
            getView().showNoConnectionErrorMessage();
        }
    }

    @VisibleForTesting
    public ConnectionHandler getConnectionHandler(){
        return connectionHandler;
    }

    @VisibleForTesting
    public abstract boolean isViewAttached();

    @VisibleForTesting
    public abstract V getView();
}
