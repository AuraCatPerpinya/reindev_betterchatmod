package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.IMaxSizeChatMessageList;
import net.minecraft.client.Minecraft;
import net.minecraft.src.client.gui.ChatLine;
import net.minecraft.src.client.gui.Gui;
import net.minecraft.src.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui implements IMaxSizeChatMessageList {
    @Shadow
    private Minecraft mc;
    @Shadow
    public List<ChatLine> chatMessageList;

    /**
     * @author AuraCat
     * @reason Makes chat history way longer :3
     */
    @Overwrite
    public void addChatMessage(String message) {
        while(this.mc.fontRenderer.getStringWidth(message) > 320) {
            int var2;
            for(var2 = 1; var2 < message.length() && this.mc.fontRenderer.getStringWidth(message.substring(0, var2 + 1)) <= 320; ++var2) {
            }

            this.addChatMessage(message.substring(0, var2));
            message = message.substring(var2);
        }

        this.chatMessageList.add(0, new ChatLine(message));

        while(this.chatMessageList.size() > this.betterChatMod$maxSizeChatMessageList) {
            this.chatMessageList.remove(this.chatMessageList.size() - 1);
        }

    }
}
