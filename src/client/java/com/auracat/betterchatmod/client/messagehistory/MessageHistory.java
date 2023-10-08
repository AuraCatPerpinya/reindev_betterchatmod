package com.auracat.betterchatmod.client.messagehistory;

import com.auracat.betterchatmod.client.config.ClientConfigManager;

import java.util.ArrayList;
import java.util.List;

public class MessageHistory {
    public List<String> list = new ArrayList<>();

    public void clearMessagesOverMaxSize() {
        assert ClientConfigManager.getConfig() != null;
        while (this.list.size() > ClientConfigManager.getConfig().maxSizePastSentMessages) {
            this.list.remove(this.list.size() - 1);
        }
    }

    public void addMessage(String messageContent) {
        this.list.add(0, messageContent);
        this.clearMessagesOverMaxSize();
    }
}
