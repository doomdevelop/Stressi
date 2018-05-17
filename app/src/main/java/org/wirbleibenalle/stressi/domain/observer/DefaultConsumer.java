package org.wirbleibenalle.stressi.domain.observer;

import io.reactivex.functions.Consumer;


public abstract class DefaultConsumer<T> implements Consumer<T>{
    public abstract void onCompleted();

    public abstract void onError(Throwable e);

    @Override
    public void accept(T t) throws Exception {

    }

    public abstract void onNext(T t);
}
