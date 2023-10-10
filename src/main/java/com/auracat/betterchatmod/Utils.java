package com.auracat.betterchatmod;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

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

    @SuppressWarnings("UnusedReturnValue")
    public static <T> T ensureDefaultValues(T object, Object defaultObject)
            throws NoSuchFieldException, IllegalAccessException {

        Class<?> objectClass = object.getClass();
        Class<?> defaultObjectClass = object.getClass();
        
        Field[] objectFields = objectClass.getDeclaredFields();

        for (Field field : objectFields) {
            boolean isPrivate = false;
            if (!field.isAccessible()) {
                isPrivate = true;
                field.setAccessible(true);
            }

            String fieldName = field.getName();
            Field defaultField = defaultObjectClass.getDeclaredField(fieldName);

            Object value = field.get(object);

            if (value == null) {
                Object defaultValue = defaultField.get(defaultObject);
                field.set(object, defaultValue);
            }
            if (isPrivate) {
                field.setAccessible(false);
            }
        }

        return object;
    }
}
