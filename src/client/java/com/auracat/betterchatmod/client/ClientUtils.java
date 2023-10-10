package com.auracat.betterchatmod.client;

import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.auracat.betterchatmod.client.config.TextSeparators;

import java.util.List;

public class ClientUtils {
    public static int searchNextTextSeparator(int increment, int currentCursorPosition, String text) {
        assert ClientConfigManager.getConfig() != null;
        TextSeparators textSeparators = ClientConfigManager.getConfig().textSeparators;
        List<Character> separators = textSeparators.getMergedList();

        Character currentSeparatorChar = null;
        boolean firstCharIsSeparator = false;

        int initialIndex = currentCursorPosition;
        int returnIndex = currentCursorPosition;

        // increment < 0 means backwards, from right to left
        if (increment < 0) {
            initialIndex--;
        }

        for (int i = initialIndex; i <= text.length(); i = i + increment) {
            if (i >= text.length()) {
                returnIndex = text.length();
                break;
            }
            if (i < 0) {
                returnIndex = 0;
                break;
            }
            Character c = text.charAt(i);
            if (
                    currentSeparatorChar != null
                            && !c.equals(currentSeparatorChar)
            ) {
                returnIndex = i;
                break;
            }
            if (separators.contains(c)) {
                currentSeparatorChar = c;
                if (i == initialIndex) {
                    firstCharIsSeparator = true;
                }
                if (!firstCharIsSeparator) {
                    returnIndex = i;
                    break;
                }
            }
        }

        return returnIndex;
    }


}
