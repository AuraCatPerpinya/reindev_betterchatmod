package com.auracat.betterchatmod.client;

import com.auracat.betterchatmod.BetterChatMod;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.fox2code.foxloader.loader.ClientMod;

import java.io.IOException;

public class BetterChatModClient extends BetterChatMod implements ClientMod {
    @Override
    public void onInit() {
        System.out.println("BetterChatMod client initializing");

        try {
            ClientConfigManager.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
