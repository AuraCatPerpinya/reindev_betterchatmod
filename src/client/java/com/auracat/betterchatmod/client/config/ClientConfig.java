package com.auracat.betterchatmod.client.config;

import com.auracat.betterchatmod.config.Config;

public class ClientConfig extends Config {
    public Integer maxSizeChatMessageList = null;
    public Integer maxSizePastSentMessages = null;

    public static ClientConfig defaultValues() {
        ClientConfig config = new ClientConfig();
        config.maxSizeChatMessageList = 1024;
        config.maxSizePastSentMessages = 1024;
        return config;
    }

    /**
     * Ensures that every property, if null, is at least set to a default value.
     * @return boolean - True if any value changed
     */
    public boolean ensureProperties() {
        boolean didAnyValueChange = false;

        ClientConfig defaults = ClientConfig.defaultValues();
        if (this.maxSizeChatMessageList == null) {
            this.maxSizeChatMessageList = defaults.maxSizeChatMessageList;
            didAnyValueChange = true;
        }
        if (this.maxSizePastSentMessages == null) {
            this.maxSizePastSentMessages = defaults.maxSizePastSentMessages;
            didAnyValueChange = true;
        }
        return didAnyValueChange;
    }
}
