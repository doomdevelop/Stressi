package org.wirbleibenalle.stressi.domain.usecase;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by and on 26.10.16.
 */
public abstract class UseCase {
    private Scheduler newThreadScheduler;
    private Scheduler androidThread;
    protected Observable observable;

    public UseCase() {
        this.newThreadScheduler = Schedulers.newThread();
        this.androidThread = AndroidSchedulers.mainThread();
    }

    public UseCase(Scheduler newThreadScheduler, Scheduler androidThread) {
        this.newThreadScheduler = newThreadScheduler;
        this.androidThread = androidThread;
    }


    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable buildUseCaseObservable();


    /**
     * Executes the current use case.
     */
    public void execute(Observer observer) {
        observable = buildUseCaseObservable()
            .subscribeOn(newThreadScheduler)
            .observeOn(androidThread);
        observable.subscribeWith(observer);
    }

    /**
     * Unsubscribes from current {@link rx.Subscription}.
     */
    public abstract void dispose();

    public abstract void unsubscribe();
}
