package com.auracat.betterchatmod.client;

import com.auracat.betterchatmod.BetterChatMod;
import com.auracat.betterchatmod.Utils;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.auracat.betterchatmod.client.messagehistory.MessageHistory;
import com.auracat.betterchatmod.client.messagehistory.MessageHistoryCursor;
import com.auracat.betterchatmod.client.modcompat.ModCompatCommandAutocomplete;
import com.fox2code.foxloader.loader.ClientMod;
import net.minecraft.client.Minecraft;

import java.io.IOException;

@SuppressWarnings("unused")
public class BetterChatModClient extends BetterChatMod implements ClientMod {
    private static MessageHistory messageHistory = new MessageHistory(Minecraft.getInstance());
    private static MessageHistoryCursor messageHistoryCursor = new MessageHistoryCursor();

    public static MessageHistory getMessageHistory() {
        return messageHistory;
    }
    public static void setMessageHistory(MessageHistory messageHistory) {
        BetterChatModClient.messageHistory = messageHistory;
    }

    public static MessageHistoryCursor getMessageHistoryCursor() {
        return messageHistoryCursor;
    }
    public static void setMessageHistoryCursor(MessageHistoryCursor messageHistoryCursor) {
        BetterChatModClient.messageHistoryCursor = messageHistoryCursor;
    }

    public void addModCompatHooks() {
        ModCompatCommandAutocomplete compatCommandAutocomplete = new ModCompatCommandAutocomplete();
        this.modCompatHooks.put(compatCommandAutocomplete.getModId(), compatCommandAutocomplete);
    }

    @Override
    public void onInit() {
        Utils.log("Client initializing");
        addModCompatHooks();
        try {
            ClientConfigManager.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Utils.log("Client initialized");
    }
}
