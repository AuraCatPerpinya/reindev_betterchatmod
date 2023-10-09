package com.auracat.betterchatmod.client.messagehistory;

import com.auracat.betterchatmod.client.config.ClientConfigManager;

import java.util.ArrayList;
import java.util.List;

public class MessageHistory {
    private List<String> list = new ArrayList<>();

    public List<String> getList() {
        return list;
    }
    public void setList(List<String> list) {
        this.list = list;
    }

    public void clearMessagesOverMaxSize() {
        assert ClientConfigManager.getConfig() != null;
        while (this.list.size() > ClientConfigManager.getConfig().maxSizeMessageHistory) {
            this.list.remove(this.list.size() - 1);
        }
    }

    public void addMessage(String messageContent) {
        this.list.add(0, messageContent);
        this.clearMessagesOverMaxSize();
    }
}
