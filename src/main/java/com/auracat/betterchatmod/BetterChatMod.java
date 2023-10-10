package com.auracat.betterchatmod;

import com.auracat.betterchatmod.modcompat.ModCompatHook;
import com.fox2code.foxloader.loader.Mod;

import java.util.HashMap;

@SuppressWarnings("RedundantMethodOverride")
public class BetterChatMod extends Mod {
    public final HashMap<String, ModCompatHook> modCompatHooks = new HashMap<>();

    public void initializeModCompatHooks() {
        Utils.log("Initializing mod compatibility hooks");
        modCompatHooks.forEach(
                (string, modCompatHook) -> modCompatHook.initialize()
        );
    }

    @Override
    public void onPreInit() {}

    @Override
    public void onPostInit() {
        Utils.log("Post-initializing");
        initializeModCompatHooks();
        Utils.log("Post-initialized");
    }
}
