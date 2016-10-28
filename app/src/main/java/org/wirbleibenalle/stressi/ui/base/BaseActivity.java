package org.wirbleibenalle.stressi.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;

/**
 * Created by and on 26.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity implements Presenter.View {

    protected Presenter presenter;

    protected abstract void initializeDagger();

    protected abstract void initializePresenter();

    public abstract int getLayoutId();

    private String toolbarTitleKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initializeButterKnife();
        initializeDagger();
        initializePresenter();

//        if (presenter != null) {
        presenter.initialize(getIntent().getExtras());
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.start();
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
            presenter.finalizeView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable(SAVE_STATE_TRANSLATIONS_KEY, (Serializable) getTranslations());
    }

    private void initializeButterKnife() {
        ButterKnife.bind(this);
    }

//    @Nullable
//    @OnClick({R.id.ic_toolbar_settings, R.id.ic_toolbar_home})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ic_toolbar_settings:
//                presenter.onSettingsClick();
//                break;
//            case R.id.ic_toolbar_home:
//                presenter.onHomeClick();
//                break;
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
