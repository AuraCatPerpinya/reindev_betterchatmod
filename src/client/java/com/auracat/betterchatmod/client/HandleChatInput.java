package com.auracat.betterchatmod.client;

import com.auracat.betterchatmod.client.messagehistory.MessageHistory;
import com.auracat.betterchatmod.client.messagehistory.MessageHistoryCursor;
import net.minecraft.src.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class HandleChatInput {
    public static void handleChatInput(
            GuiTextField textField,
            char eventChar,
            int eventKey,
            CallbackInfo ci
    ) {
        if (textField.isEnabled && textField.isFocused) {
            String textFieldCurrentText = textField.getText();
            MessageHistory messageHistory = BetterChatModClient.getMessageHistory();
            List<String> msgHistoryList = messageHistory.getList();
            MessageHistoryCursor msgHistoryCursor = BetterChatModClient.getMessageHistoryCursor();

            int originalMsgCursorIndex = msgHistoryCursor.getIndex();
            int currentMsgCursorIndex = msgHistoryCursor.getIndex();

            if (eventKey == Keyboard.KEY_ESCAPE) {
                currentMsgCursorIndex = msgHistoryCursor.setIndex(-1);
            } else if (eventKey == Keyboard.KEY_RETURN) {
                currentMsgCursorIndex = msgHistoryCursor.setIndex(-1);

                String trimmedCurrentText = textFieldCurrentText.trim();
                if (trimmedCurrentText.length() > 0) {
                    messageHistory.addMessage(trimmedCurrentText);
                }
            } else if (eventKey == Keyboard.KEY_UP) {
                if (currentMsgCursorIndex == -1) {
                    msgHistoryCursor.setOriginallyTyped(textFieldCurrentText);
                }
                currentMsgCursorIndex = msgHistoryCursor.incrementIndex();
                if (currentMsgCursorIndex >= msgHistoryList.size()) {
                    currentMsgCursorIndex = msgHistoryCursor.decrementIndex();
                }

                if (currentMsgCursorIndex != originalMsgCursorIndex) {
                    textField.setText(msgHistoryList.get(currentMsgCursorIndex));
                }

            } else if (eventKey == Keyboard.KEY_DOWN) {
                currentMsgCursorIndex = msgHistoryCursor.decrementIndex();

                if (currentMsgCursorIndex < -1) {
                    currentMsgCursorIndex = msgHistoryCursor.incrementIndex();
                }

                //noinspection StatementWithEmptyBody
                if (currentMsgCursorIndex == -1 && originalMsgCursorIndex == -1) {
                } else if (currentMsgCursorIndex == -1) {
                    textField.setText(msgHistoryCursor.getOriginallyTyped());
                } else {
                    textField.setText(msgHistoryList.get(currentMsgCursorIndex));
                }
            } else if (eventKey == Keyboard.KEY_HOME) {
                textField.setCursorPosition(0);
            } else if (eventKey == Keyboard.KEY_END) {
                textField.setCursorPosition(textFieldCurrentText.length());
            }

            if (currentMsgCursorIndex != originalMsgCursorIndex) {
                textField.setCursorPosition(textFieldCurrentText.length());
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                HandleChatInput.handleCtrlCombinations(
                        textField,
                        eventChar,
                        eventKey,
                        ci
                );
            }
        }
    }

    public static void handleCtrlCombinations(
            GuiTextField textField,
            char ignoredEventChar,
            int eventKey,
            CallbackInfo ci
    ) {
            String textFieldCurrentText = textField.getText();
            int currentCursorPosition = textField.cursorPosition;

            if (eventKey == Keyboard.KEY_BACK) {
                // Deletes a group of characters (typically a word) backwards, from right to left
                if (textFieldCurrentText.length() > 0) {
                    int textSeparatorIndex = ClientUtils.searchNextTextSeparator(
                            -1,
                            currentCursorPosition,
                            textFieldCurrentText
                    );
                    if (textSeparatorIndex > 0) {
                        textSeparatorIndex++;
                    }

                    String newText = textFieldCurrentText.substring(0, textSeparatorIndex);
                    if (currentCursorPosition < textFieldCurrentText.length()) {
                        newText = newText + textFieldCurrentText.substring(currentCursorPosition);
                    }

                    textField.setText(newText);
                    textField.setCursorPosition(textSeparatorIndex);
                }

            } else if (eventKey == Keyboard.KEY_DELETE) {
                // Deletes a group of characters forwards, from left to right
                int textSeparatorIndex = ClientUtils.searchNextTextSeparator(
                        1,
                        currentCursorPosition,
                        textFieldCurrentText
                );

                String newText = textFieldCurrentText.substring(0, currentCursorPosition);
                if (textSeparatorIndex < textFieldCurrentText.length()) {
                    newText = newText + textFieldCurrentText.substring(textSeparatorIndex);
                }

                textField.setText(newText);
            }

            if (eventKey == Keyboard.KEY_LEFT) {
                // Moves the cursor to the next text separator at its left
                if (textFieldCurrentText.length() > 0) {
                    int textSeparatorIndex = ClientUtils.searchNextTextSeparator(
                            -1,
                            currentCursorPosition,
                            textFieldCurrentText
                    );
                    if (textSeparatorIndex > 0) {
                        textSeparatorIndex++;
                    }

                    textField.setCursorPosition(textSeparatorIndex);
                }
            } else if (eventKey == Keyboard.KEY_RIGHT) {
                // Moves the cursor to the next text separator at its right
                int textSeparatorIndex = ClientUtils.searchNextTextSeparator(
                        1,
                        currentCursorPosition,
                        textFieldCurrentText
                );

                textField.setCursorPosition(textSeparatorIndex);
            }

            ci.cancel();
    }
}
