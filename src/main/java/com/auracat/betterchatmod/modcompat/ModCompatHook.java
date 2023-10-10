package com.auracat.betterchatmod.modcompat;

import com.auracat.betterchatmod.utils.Utils;
import com.fox2code.foxloader.loader.ModContainer;

public interface ModCompatHook {
    boolean isModPresent();
    String getModId();
    ModContainer getModContainer();

    default void initialize() {
        Utils.log("Initializing compatibility hook for " + this.getModId());
    }
}
