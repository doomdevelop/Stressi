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

    public void start() {
        isViewAlive.set(true);
    }

    public void finalizeView() {
            isViewAlive.set(false);
    }

//    public void onSettingsClick() {
//        ((ActionBarView) view).openSettingsView();
//    }
//
//    public void onHomeClick() {
//        ((ActionBarView) view).openHomeView(null);
//    }

    protected void handleErrorCase(ResponseError error) {
        boolean userLogged = error.getErrorCode() != ERROR_NOT_FOUND;
        view.showError(error.getErrorMessage());
    }

    public interface View {
        void showError(String errorMessage);
    }
}
