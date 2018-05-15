package org.wirbleibenalle.stressi.domain.usecase;


import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T> {
    private final Scheduler newThreadScheduler;
    private final Scheduler androidThread;

    public UseCase() {
        this.newThreadScheduler = Schedulers.io();
        this.androidThread = AndroidSchedulers.mainThread();
    }

    public UseCase(Scheduler newThreadScheduler, Scheduler androidThread) {
        this.newThreadScheduler = newThreadScheduler;
        this.androidThread = androidThread;
    }

    /**
     * Builds an {@link io.reactivex.Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable<T> buildUseCaseObservable();


    /**
     * Executes the current use case.
     */
    public void execute(DisposableObserver<T> observer, CompositeDisposable compositeDisposable) {
        Observable<T> observable = buildUseCaseObservable()
            .subscribeOn(newThreadScheduler)
            .observeOn(androidThread);
        compositeDisposable.add(observable.subscribeWith(observer));
    }
}
