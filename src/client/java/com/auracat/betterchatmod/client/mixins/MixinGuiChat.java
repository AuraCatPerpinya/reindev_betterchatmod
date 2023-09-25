package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.ChatInputLogs;
import com.auracat.betterchatmod.client.ChatInputLogsCursor;
import com.auracat.betterchatmod.client.IWithChatInputLogs;
import com.auracat.betterchatmod.client.IWithChatInputLogsCursor;
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
public class MixinGuiChat extends GuiScreen implements IWithChatInputLogs, IWithChatInputLogsCursor {
    @Shadow
    public GuiTextField chat;

    @Unique
    ChatInputLogs betterChatMod$chatInputLogs = ((IWithChatInputLogs) this.mc).betterChatMod$chatInputLogs;

    @Inject(method = "keyTyped", at = @At(value = "HEAD"))
    public void keyTypedMixin(char eventChar, int eventKey, CallbackInfo ci) {
        ChatInputLogsCursor cursor = this.betterChatMod$chatInputLogsCursor;
        ChatInputLogs inputLogs = this.betterChatMod$chatInputLogs;

        int currentCursorIndex = cursor.getIndex();

        if (eventKey == Keyboard.KEY_ESCAPE) {
            currentCursorIndex = cursor.setIndex(-1);
        } else if (eventKey == Keyboard.KEY_RETURN && this.chat.isEnabled) {
                inputLogs.addMessage(this.chat.getText());
        } else if (eventKey == Keyboard.KEY_UP) {
            if (currentCursorIndex == -1) {
                this.betterChatMod$chatInputLogsCursor.setOriginallyTyped(this.chat.getText());
            }
            currentCursorIndex = cursor.incrementIndex();
            if (currentCursorIndex >= inputLogs.list.size()) {
                currentCursorIndex = cursor.decrementIndex();
            }

            this.chat.setText(inputLogs.list.get(currentCursorIndex));

        } else if (eventKey == Keyboard.KEY_DOWN) {
            currentCursorIndex = cursor.decrementIndex();
            if (currentCursorIndex < -1) {
                currentCursorIndex = cursor.incrementIndex();
            }

            if (currentCursorIndex == -1) {
                this.chat.setText(cursor.getOriginallyTyped());
            } else {
                this.chat.setText(inputLogs.list.get(currentCursorIndex));
            }
        }
    }
}
