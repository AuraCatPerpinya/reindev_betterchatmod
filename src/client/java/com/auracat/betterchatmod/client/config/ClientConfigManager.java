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
                ClientConfig.defaultValues()
        );
        boolean didAnythingChange = readConfig.ensureProperties();
        if (didAnythingChange) {
            writeConfigFile(filePathString, readConfig);
        }
        Utils.log(
                "\n" + "maxSizeChatMessageList: " + readConfig.maxSizeChatMessageList
                        + "\n" + "maxSizePastSentMessages: " + readConfig.maxSizePastSentMessages
        );

        config = readConfig;
    }
}
