package com.auracat.betterchatmod.modcompat;

import com.auracat.betterchatmod.Utils;
import com.fox2code.foxloader.loader.ModContainer;

public interface ModCompatHook {
    @SuppressWarnings("unused")
    boolean isModPresent();
    String getModId();
    @SuppressWarnings("unused")
    ModContainer getModContainer();

    default void initialize() {
        Utils.log("Initializing compatibility hook for " + this.getModId());
    }
}
