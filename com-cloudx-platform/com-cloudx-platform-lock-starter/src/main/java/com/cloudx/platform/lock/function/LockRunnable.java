package com.cloudx.platform.lock.function;

@FunctionalInterface
public interface LockRunnable<T> {

    T run() throws Throwable;
}
