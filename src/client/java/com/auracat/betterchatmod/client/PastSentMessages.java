package com.auracat.betterchatmod.client;

import java.util.ArrayList;
import java.util.List;

public class PastSentMessages {
    public List<String> list = new ArrayList<>();
    public int maxSize = 1024;

    public void clearMessagesOverMaxSize() {
        while (this.list.size() > this.maxSize) {
            this.list.remove(this.list.size() - 1);
        }
    }

    public void addMessage(String messageContent) {
        this.list.add(0, messageContent);
        this.clearMessagesOverMaxSize();
    }
}
