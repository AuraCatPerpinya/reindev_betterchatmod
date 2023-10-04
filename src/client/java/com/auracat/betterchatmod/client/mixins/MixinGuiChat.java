package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.PastSentMessages;
import com.auracat.betterchatmod.client.PastSentMessagesCursor;
import com.auracat.betterchatmod.client.IWithPastSentMessages;
import com.auracat.betterchatmod.client.IWithPastSentMessagesCursor;
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

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen implements IWithPastSentMessages, IWithPastSentMessagesCursor {
    @Shadow
    public GuiTextField chat;

    @Unique
    PastSentMessages betterChatMod$pastSentMessages = ((IWithPastSentMessages) this.mc).betterChatMod$pastSentMessages;

    @Inject(method = "keyTyped", at = @At(value = "HEAD"))
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
        }

    }
}
