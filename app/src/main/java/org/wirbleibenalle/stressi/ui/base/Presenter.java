package org.wirbleibenalle.stressi.ui.base;

import android.os.Bundle;

import org.wirbleibenalle.stressi.data.remote.ResponseError;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by and on 26.10.16.
 */

public abstract class Presenter<T extends Presenter.View> {
    private static final int ERROR_NOT_FOUND = 404;
    protected T view;

    protected boolean retried = false;

    protected AtomicBoolean isViewAlive = new AtomicBoolean();

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public void initialize(Bundle extras) {
    }

    public void onStart() {
        isViewAlive.set(true);
    }

    public void onStop() {
            isViewAlive.set(false);
    }

    protected void showError(String errorMessage){
        view.showError(errorMessage);
    }

    protected void showMessage(String message){
        view.showMessage(message);
    }

    public interface View {
        void showError(String errorMessage);

        void showMessage(String message);
    }
}
