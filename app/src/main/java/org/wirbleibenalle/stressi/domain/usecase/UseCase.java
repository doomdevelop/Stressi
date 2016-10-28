package org.wirbleibenalle.stressi.domain.usecase;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by and on 26.10.16.
 */

public abstract class UseCase {
    private Subscription subscription = Subscriptions.empty();
    private int id;
    private Scheduler newThreadScheduler;
    private Scheduler androidThread;

    public int getId() {
        return id;
    }

    public UseCase() {
        this.id = (int) System.currentTimeMillis();
        this.newThreadScheduler = Schedulers.newThread();
        this.androidThread = AndroidSchedulers.mainThread();
    }

    public UseCase(Scheduler newThreadScheduler, Scheduler androidThread) {
        this.id = (int) System.currentTimeMillis();
        this.newThreadScheduler = newThreadScheduler;
        this.androidThread = androidThread;
    }


    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable buildUseCaseObservable();

    /**
     * Executes the current use case.
     *
     * @param useCaseobserver The guy who will be listen to the observable build with {@link #buildUseCaseObservable()}.
     */
    @SuppressWarnings("unchecked")
    public void execute(Observer useCaseobserver) {
        this.subscription = this.buildUseCaseObservable()
            .subscribeOn(newThreadScheduler)
            .observeOn(androidThread)
            .subscribe(useCaseobserver);
    }

    /**
     * Unsubscribes from current {@link rx.Subscription}.
     */
    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
