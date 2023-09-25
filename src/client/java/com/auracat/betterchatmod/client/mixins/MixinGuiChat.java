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
            PastSentMessagesCursor cursor = this.betterChatMod$pastSentMessagesCursor;
            PastSentMessages pastSentMessages = this.betterChatMod$pastSentMessages;

            int previousCursorIndex = cursor.getIndex();
            int currentCursorIndex = cursor.getIndex();

            if (eventKey == Keyboard.KEY_ESCAPE) {
                currentCursorIndex = cursor.setIndex(-1);
            } else if (eventKey == Keyboard.KEY_RETURN) {
                currentCursorIndex = cursor.setIndex(-1);

                String message = this.chat.getText().trim();
                if (message.length() > 0) {
                    pastSentMessages.addMessage(message);
                }
            } else if (eventKey == Keyboard.KEY_UP) {
                if (currentCursorIndex == -1) {
                    this.betterChatMod$pastSentMessagesCursor.setOriginallyTyped(this.chat.getText());
                }
                currentCursorIndex = cursor.incrementIndex();
                if (currentCursorIndex >= pastSentMessages.list.size()) {
                    currentCursorIndex = cursor.decrementIndex();
                }

                if (currentCursorIndex != previousCursorIndex) {
                    this.chat.setText(pastSentMessages.list.get(currentCursorIndex));
                }

            } else if (eventKey == Keyboard.KEY_DOWN) {
                currentCursorIndex = cursor.decrementIndex();

                if (currentCursorIndex < -1) {
                    currentCursorIndex = cursor.incrementIndex();
                }

                if (currentCursorIndex == -1 && previousCursorIndex == -1) {
                } else if (currentCursorIndex == -1) {
                    this.chat.setText(cursor.getOriginallyTyped());
                } else {
                    this.chat.setText(pastSentMessages.list.get(currentCursorIndex));
                }
            } else if (eventKey == Keyboard.KEY_HOME) {
                this.chat.setCursorPosition(0);
            } else if (eventKey == Keyboard.KEY_END) {
                this.chat.setCursorPosition(this.chat.getText().length());
            }

            if (currentCursorIndex != previousCursorIndex) {
                this.chat.setCursorPosition(this.chat.getText().length());
            }
        }

    }
}
