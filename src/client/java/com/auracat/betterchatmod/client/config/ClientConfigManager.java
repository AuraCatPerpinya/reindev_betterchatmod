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
        ClientConfig defaultConfig = new ClientConfig();

        ClientConfig config = readConfigFile(
                filePathString,
                ClientConfig.class,
                defaultConfig
        );

        try {
            Utils.ensureDefaultValues(config, defaultConfig);
            Utils.ensureDefaultValues(config.textSeparators, new TextSeparators());
        } catch (Error | Exception e) {
            throw new RuntimeException(e);
        }

        writeConfigFile(filePathString, config);

        ClientConfigManager.config = config;
    }
}
