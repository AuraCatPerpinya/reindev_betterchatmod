package com.auracat.betterchatmod;

import com.auracat.betterchatmod.modcompat.ModCompatHook;
import com.fox2code.foxloader.loader.Mod;

import java.util.HashMap;

public class BetterChatMod extends Mod {
    public HashMap<String, ModCompatHook> modCompatHooks = new HashMap<>();

    @Override
    public void onPreInit() {}
}
