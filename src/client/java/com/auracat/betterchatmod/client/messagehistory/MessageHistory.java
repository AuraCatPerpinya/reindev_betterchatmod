package com.auracat.betterchatmod.client.messagehistory;

import com.auracat.betterchatmod.client.BetterChatModClient;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void filterMessageHistory() {
        Minecraft minecraft = Minecraft.getInstance();

        MessageHistory messageHistory = BetterChatModClient.getMessageHistory();
        List<String> msgHistoryList = messageHistory.getList();
        Stream<String> stream = msgHistoryList.stream();

        assert ClientConfigManager.getConfig() != null;
        switch (ClientConfigManager.getConfig().messageHistoryIsPerWorld) {
            case NO:
                break;
            case ONLY_COMMANDS:
                List<String> collectOnlyCommands = stream
                        .filter(minecraft::lineIsCommand)
                        .collect(Collectors.toList());
                messageHistory.setList(collectOnlyCommands);
                break;
            case ONLY_NORMAL_MSGS:
                List<String> collectOnlyNormalMsgs = stream
                        .filter((message) -> !(minecraft.lineIsCommand(message)))
                        .collect(Collectors.toList());
                messageHistory.setList(collectOnlyNormalMsgs);
                break;
            case YES:
                msgHistoryList.clear();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ClientConfigManager.getConfig().messageHistoryIsPerWorld);
        }
    }
}
