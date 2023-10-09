package com.auracat.betterchatmod.modcompat;

import com.fox2code.foxloader.loader.ModContainer;
import com.fox2code.foxloader.loader.ModLoader;

public interface ModCompatHook {
    boolean isModPresent();
    String getModId();
    ModContainer getModContainer();
}
