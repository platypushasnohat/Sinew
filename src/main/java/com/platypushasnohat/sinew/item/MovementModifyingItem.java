package com.platypushasnohat.sinew.item;

public interface MovementModifyingItem {

    float getUseMovementMultiplier();

    boolean canSprintWhileUsing();
}
