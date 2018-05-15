package org.wirbleibenalle.stressi.ui.base;

import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.wirbleibenalle.stressi.stressfaktor.R;

import butterknife.ButterKnife;

public abstract class BaseActivity<V extends BaseContract.View, P extends BaseContract.Presenter<V>>
    extends AppCompatActivity implements BaseContract.View {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    protected P presenter;

    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initializeButterKnife();
        initializeDagger();
        initializeToolbar();

        presenter = initPresenter();
        presenter.attachLifecycle(getLifecycle());
        presenter.attachView((V) this);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachLifecycle(getLifecycle());
        presenter.detachView();
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar snackbar = createSnackbar(errorMessage);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorMainRed));
        snackbar.show();
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = createSnackbar(message);
        snackbar.show();
    }

    protected abstract void initializeDagger();

    public abstract int getLayoutId();

    protected abstract Snackbar createSnackbar(String message);

    protected abstract void initializeToolbar();

    protected abstract void setTitle(String title);

    protected abstract void animateTitle();

    private void initializeButterKnife() {
        ButterKnife.bind(this);
    }

    protected abstract P initPresenter();
}
