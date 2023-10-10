package com.auracat.betterchatmod.client.utils;

import com.auracat.betterchatmod.client.BetterChatModClient;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.auracat.betterchatmod.client.messagehistory.MessageHistory;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.stream.Collectors;

public class LeavingWorld {
    public static void filterMessageHistory(Minecraft minecraft) {
        MessageHistory messageHistory = BetterChatModClient.getMessageHistory();
        List<String> msgHistoryList = messageHistory.getList();

        assert ClientConfigManager.getConfig() != null;
        switch (ClientConfigManager.getConfig().messageHistoryIsPerWorld) {
            case NO:
                break;
            case ONLY_COMMANDS:
                List<String> collectOnlyCommands = msgHistoryList
                        .stream()
                        .filter(minecraft::lineIsCommand)
                        .collect(Collectors.toList());
                messageHistory.setList(collectOnlyCommands);
                break;
            case ONLY_NORMAL_MSGS:
                List<String> collectOnlyNormalMsgs = messageHistory.getList()
                        .stream()
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
