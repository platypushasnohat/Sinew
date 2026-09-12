package com.platypushasnohat.sinew.block;

public interface Brushable {

    default boolean brush(long startTick) {
        return false;
    }
}
