package org.wirbleibenalle.stressi.ui.base;

import android.arch.lifecycle.ViewModel;

public final class BaseViewModel<V extends BaseContract.View, P extends BaseContract.Presenter<V>>
    extends ViewModel {

    private P presenter;

    void setPresenter(P presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    P getPresenter() {
        return this.presenter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        presenter.onPresenterDestroy();
        presenter = null;
    }
}
