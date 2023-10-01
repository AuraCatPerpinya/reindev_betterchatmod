package com.auracat.betterchatmod;

import java.io.IOException;
import java.io.Reader;

public class Utils {
    public static String readAllCharactersOneByOne(Reader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        int nextChar;
        while ((nextChar = reader.read()) != -1) {
            content.append((char) nextChar);
        }
        return String.valueOf(content);
    }

    public static void methodNotImplemented() {
        throw new Error("Function not implemented");
    }

    public static void log(String text) {
        System.out.println("[BetterChatMod] " + text);
    }
}
