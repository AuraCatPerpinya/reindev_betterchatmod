package com.auracat.betterchatmod.client.config;

import com.auracat.betterchatmod.config.Config;

public class ClientConfig extends Config {
    public Integer maxSizeChatMessageList = 1024;
    public Integer maxSizePastSentMessages = 1024;
    public TextSeparators textSeparators = new TextSeparators();
}
