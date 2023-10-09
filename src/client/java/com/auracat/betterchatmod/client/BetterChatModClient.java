package com.auracat.betterchatmod.client;

import com.auracat.betterchatmod.BetterChatMod;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.auracat.betterchatmod.client.modcompat.ModCompatCommandAutocomplete;
import com.fox2code.foxloader.loader.ClientMod;

import java.io.IOException;

public class BetterChatModClient extends BetterChatMod implements ClientMod {
    @Override
    public void onInit() {
        System.out.println("BetterChatMod client initializing");

        ModCompatCommandAutocomplete compatCommandAutocomplete = new ModCompatCommandAutocomplete();
        this.modCompatHooks.put(compatCommandAutocomplete.getModId(), compatCommandAutocomplete);

        try {
            ClientConfigManager.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("BetterChatMod client initialized");
    }
}
