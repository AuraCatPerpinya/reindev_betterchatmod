package com.auracat.betterchatmod.client.messagehistory;

public class MessageHistoryCursor {
    private int index = -1;
    private String originallyTyped = "";

    public int getIndex() {
        return index;
    }
    public int setIndex(int newValue) {
        index = newValue;

        return index;
    }
    public int incrementIndex() {
        return ++index;
    }
    public int decrementIndex() {
        return --index;
    }

    public String getOriginallyTyped() {
        return originallyTyped;
    }
    public void setOriginallyTyped(String newValue) {
        this.originallyTyped = newValue;
    }
}
