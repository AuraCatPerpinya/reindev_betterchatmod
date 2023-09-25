package com.auracat.betterchatmod.client;

import com.auracat.betterchatmod.BetterChatMod;
import com.fox2code.foxloader.loader.ClientMod;

public class BetterChatModClient extends BetterChatMod implements ClientMod {
    public void onInit() {
        System.out.println("BetterChatMod client initializing");
    }
}
