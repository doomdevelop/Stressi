package org.wirbleibenalle.stressi.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.wirbleibenalle.stressi.stressfaktor.R;
import org.wirbleibenalle.stressi.ui.animation.BounceInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by and on 26.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity implements Presenter.View {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.txt_toolbar_title)
    protected TextView tvTitle;
    @BindView(R.id.coordinatorLayout)
    protected CoordinatorLayout coordinatorLayout;

    protected Presenter presenter;

    protected abstract void initializeDagger();

    protected abstract void initializePresenter();

    public abstract int getLayoutId();

    private String toolbarTitleKey;

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

    private Snackbar createSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initializeButterKnife();
        initializeDagger();
        initializePresenter();
        initializeToolbar();
        presenter.initialize(getIntent().getExtras());
    }

    protected void initializeToolbar() {
        setSupportActionBar(toolbar);
    }

    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void animateTitle() {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        tvTitle.startAnimation(myAnim);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    private void initializeButterKnife() {
        ButterKnife.bind(this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
