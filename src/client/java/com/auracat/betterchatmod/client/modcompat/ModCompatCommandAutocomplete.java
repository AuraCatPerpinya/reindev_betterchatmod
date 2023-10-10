package com.auracat.betterchatmod.client.modcompat;

import com.auracat.betterchatmod.modcompat.ModCompatHook;
import com.fox2code.foxloader.loader.ModContainer;
import com.fox2code.foxloader.loader.ModLoader;

public class ModCompatCommandAutocomplete implements ModCompatHook {
    final String modId = "jelliedautocomplete";

    @SuppressWarnings("unused")
    @Override
    public boolean isModPresent() {
        return this.getModContainer() != null;
    }

    @Override
    public String getModId() {
        return this.modId;
    }

    @Override
    public ModContainer getModContainer() {
        return ModLoader.getModContainer(this.getModId());
    }
}
