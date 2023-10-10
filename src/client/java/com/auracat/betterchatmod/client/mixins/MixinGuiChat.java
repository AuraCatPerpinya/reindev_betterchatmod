package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.HandleChatInput;
import net.minecraft.src.client.gui.GuiChat;
import net.minecraft.src.client.gui.GuiScreen;
import net.minecraft.src.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {
    @Shadow
    public GuiTextField chat;

    @Inject(method = "keyTyped", at = @At(value = "HEAD"), cancellable = true)
    public void onKeyTyped(char eventChar, int eventKey, CallbackInfo ci) {
        HandleChatInput.handleChatInput(this.chat, eventChar, eventKey, ci);
    }
}
