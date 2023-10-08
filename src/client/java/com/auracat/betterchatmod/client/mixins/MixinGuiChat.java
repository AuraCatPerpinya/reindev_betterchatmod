package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.PastSentMessages;
import com.auracat.betterchatmod.client.PastSentMessagesCursor;
import com.auracat.betterchatmod.client.IWithPastSentMessages;
import com.auracat.betterchatmod.client.IWithPastSentMessagesCursor;
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
public class MixinGuiChat extends GuiScreen implements IWithPastSentMessages, IWithPastSentMessagesCursor {
    @Shadow
    public GuiTextField chat;

    @Unique
    PastSentMessages betterChatMod$pastSentMessages = ((IWithPastSentMessages) this.mc).betterChatMod$pastSentMessages;

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
            PastSentMessagesCursor sentMsgsCursor = this.betterChatMod$pastSentMessagesCursor;
            PastSentMessages pastSentMessages = this.betterChatMod$pastSentMessages;

            int previousMsgCursorIndex = sentMsgsCursor.getIndex();
            int currentMsgCursorIndex = sentMsgsCursor.getIndex();

            if (eventKey == Keyboard.KEY_ESCAPE) {
                currentMsgCursorIndex = sentMsgsCursor.setIndex(-1);
            } else if (eventKey == Keyboard.KEY_RETURN) {
                currentMsgCursorIndex = sentMsgsCursor.setIndex(-1);

                String message = this.chat.getText().trim();
                if (message.length() > 0) {
                    pastSentMessages.addMessage(message);
                }
            } else if (eventKey == Keyboard.KEY_UP) {
                if (currentMsgCursorIndex == -1) {
                    this.betterChatMod$pastSentMessagesCursor.setOriginallyTyped(this.chat.getText());
                }
                currentMsgCursorIndex = sentMsgsCursor.incrementIndex();
                if (currentMsgCursorIndex >= pastSentMessages.list.size()) {
                    currentMsgCursorIndex = sentMsgsCursor.decrementIndex();
                }

                if (currentMsgCursorIndex != previousMsgCursorIndex) {
                    this.chat.setText(pastSentMessages.list.get(currentMsgCursorIndex));
                }

            } else if (eventKey == Keyboard.KEY_DOWN) {
                currentMsgCursorIndex = sentMsgsCursor.decrementIndex();

                if (currentMsgCursorIndex < -1) {
                    currentMsgCursorIndex = sentMsgsCursor.incrementIndex();
                }

                if (currentMsgCursorIndex == -1 && previousMsgCursorIndex == -1) {
                } else if (currentMsgCursorIndex == -1) {
                    this.chat.setText(sentMsgsCursor.getOriginallyTyped());
                } else {
                    this.chat.setText(pastSentMessages.list.get(currentMsgCursorIndex));
                }
            } else if (eventKey == Keyboard.KEY_HOME) {
                this.chat.setCursorPosition(0);
            } else if (eventKey == Keyboard.KEY_END) {
                this.chat.setCursorPosition(this.chat.getText().length());
            }

            if (currentMsgCursorIndex != previousMsgCursorIndex) {
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
