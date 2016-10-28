package org.wirbleibenalle.stressi.domain.observer;

import rx.Observer;

/**
 * Created by and on 26.10.16.
 */

public class DefaultObserver<T> implements Observer<T> {
    private int useCaseId = 0;

    public void setUseCaseId(int useCaseId) {
        this.useCaseId = useCaseId;
    }

    public int getUseCaseId() {
        return useCaseId;
    }

    @Override
    public void onCompleted() {
        // no-op by default.
    }

    @Override
    public void onError(Throwable e) {
        // no-op by default.
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }
}
