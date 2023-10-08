package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.messagehistory.MessageHistory;
import com.auracat.betterchatmod.client.messagehistory.MessageHistoryCursor;
import com.auracat.betterchatmod.client.messagehistory.IWithMessageHistory;
import com.auracat.betterchatmod.client.messagehistory.IWithMessageHistoryCursor;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.auracat.betterchatmod.client.config.TextSeparators;
import net.minecraft.src.client.gui.GuiChat;
import net.minecraft.src.client.gui.GuiScreen;
import net.minecraft.src.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen implements IWithMessageHistory, IWithMessageHistoryCursor {
    @Shadow
    public GuiTextField chat;

    @Unique
    MessageHistory betterChatMod$messageHistory = ((IWithMessageHistory) this.mc).betterChatMod$messageHistory;

    @Unique
    public int betterChatMod$searchNextTextSeparator(int increment, int currentCursorPosition, String text) {
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

        System.out.println("returnIndex: " + returnIndex);
        return returnIndex;
    }

    @Inject(method = "keyTyped", at = @At(value = "HEAD"), cancellable = true)
    public void keyTypedMixin(char eventChar, int eventKey, CallbackInfo ci) {
        if (this.chat.isEnabled) {
            MessageHistoryCursor msgHistoryCursor = this.betterChatMod$messageHistoryCursor;
            MessageHistory messageHistory = this.betterChatMod$messageHistory;

            int originalMsgCursorIndex = msgHistoryCursor.getIndex();
            int currentMsgCursorIndex = msgHistoryCursor.getIndex();

            if (eventKey == Keyboard.KEY_ESCAPE) {
                currentMsgCursorIndex = msgHistoryCursor.setIndex(-1);
            } else if (eventKey == Keyboard.KEY_RETURN) {
                currentMsgCursorIndex = msgHistoryCursor.setIndex(-1);

                String message = this.chat.getText().trim();
                if (message.length() > 0) {
                    messageHistory.addMessage(message);
                }
            } else if (eventKey == Keyboard.KEY_UP) {
                if (currentMsgCursorIndex == -1) {
                    this.betterChatMod$messageHistoryCursor.setOriginallyTyped(this.chat.getText());
                }
                currentMsgCursorIndex = msgHistoryCursor.incrementIndex();
                if (currentMsgCursorIndex >= messageHistory.list.size()) {
                    currentMsgCursorIndex = msgHistoryCursor.decrementIndex();
                }

                if (currentMsgCursorIndex != originalMsgCursorIndex) {
                    this.chat.setText(messageHistory.list.get(currentMsgCursorIndex));
                }

            } else if (eventKey == Keyboard.KEY_DOWN) {
                currentMsgCursorIndex = msgHistoryCursor.decrementIndex();

                if (currentMsgCursorIndex < -1) {
                    currentMsgCursorIndex = msgHistoryCursor.incrementIndex();
                }

                if (currentMsgCursorIndex == -1 && originalMsgCursorIndex == -1) {
                } else if (currentMsgCursorIndex == -1) {
                    this.chat.setText(msgHistoryCursor.getOriginallyTyped());
                } else {
                    this.chat.setText(messageHistory.list.get(currentMsgCursorIndex));
                }
            } else if (eventKey == Keyboard.KEY_HOME) {
                this.chat.setCursorPosition(0);
            } else if (eventKey == Keyboard.KEY_END) {
                this.chat.setCursorPosition(this.chat.getText().length());
            }

            if (currentMsgCursorIndex != originalMsgCursorIndex) {
                this.chat.setCursorPosition(this.chat.getText().length());
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                String currentText = this.chat.getText();
                int currentCursorPosition = this.chat.cursorPosition;

                if (eventKey == Keyboard.KEY_BACK) {
                    // Deletes a group of characters (typically a word) backwards, from right to left
                    if (currentText.length() > 0) {
                        int textSeparatorIndex = this.betterChatMod$searchNextTextSeparator(
                                -1,
                                currentCursorPosition,
                                currentText
                        );
                        if (textSeparatorIndex > 0) {
                            textSeparatorIndex++;
                        }

                        String newText = currentText.substring(0, textSeparatorIndex);
                        if (currentCursorPosition < currentText.length()) {
                            newText = newText + currentText.substring(currentCursorPosition);
                        }

                        this.chat.setText(newText);
                        this.chat.setCursorPosition(textSeparatorIndex);
                    }

                } else if (eventKey == Keyboard.KEY_DELETE) {
                    // Deletes a group of characters forwards, from left to right
                    int textSeparatorIndex = this.betterChatMod$searchNextTextSeparator(
                            1,
                            currentCursorPosition,
                            currentText
                    );

                    String newText = currentText.substring(0, currentCursorPosition);
                    if (textSeparatorIndex < currentText.length()) {
                        newText = newText + currentText.substring(textSeparatorIndex);
                    }

                    this.chat.setText(newText);
                }

                if (eventKey == Keyboard.KEY_LEFT) {
                    // Moves the cursor to the next text separator at its left
                    if (currentText.length() > 0) {
                        int textSeparatorIndex = this.betterChatMod$searchNextTextSeparator(
                                -1,
                                currentCursorPosition,
                                currentText
                        );
                        if (textSeparatorIndex > 0) {
                            textSeparatorIndex++;
                        }

                        this.chat.setCursorPosition(textSeparatorIndex);
                    }
                } else if (eventKey == Keyboard.KEY_RIGHT) {
                    // Moves the cursor to the next text separator at its right
                    int textSeparatorIndex = this.betterChatMod$searchNextTextSeparator(
                            1,
                            currentCursorPosition,
                            currentText
                    );

                    this.chat.setCursorPosition(textSeparatorIndex);
                }

                ci.cancel();
            }
        }

    }
}
