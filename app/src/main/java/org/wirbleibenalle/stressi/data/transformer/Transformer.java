package org.wirbleibenalle.stressi.data.transformer;

public abstract class Transformer<T,K> {
    public abstract K transform(T t);
}
