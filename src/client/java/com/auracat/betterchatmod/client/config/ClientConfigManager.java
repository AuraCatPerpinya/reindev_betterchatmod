package com.auracat.betterchatmod.client.config;

import com.auracat.betterchatmod.Utils;
import com.auracat.betterchatmod.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ClientConfigManager extends ConfigManager {
    @Nullable
    private static ClientConfig config = null;

    @Nullable
    public static ClientConfig getConfig() {
        return ClientConfigManager.config;
    }

    public static void initialize() throws IOException {
        String filePathString = "reindev_betterchatmod.json";

        initializeConfigFolder("config/reindev_betterchatmod");

        ClientConfig readConfig = readConfigFile(
                filePathString,
                ClientConfig.class,
                new ClientConfig()
        );

        Utils.log(
                "\n" + "maxSizeChatMessageList: " + readConfig.maxSizeChatMessageList
                        + "\n" + "maxSizePastSentMessages: " + readConfig.maxSizePastSentMessages
        );

        writeConfigFile(filePathString, readConfig);

        config = readConfig;
    }
}
