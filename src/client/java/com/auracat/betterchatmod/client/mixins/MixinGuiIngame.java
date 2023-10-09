package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.config.ClientConfigManager;
import net.minecraft.src.client.gui.Gui;
import net.minecraft.src.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {
    @ModifyConstant(method = "addChatMessage", constant = @Constant(intValue = 50))
    private int onAddChatMessage(int constant) {
        assert ClientConfigManager.getConfig() != null;
        return ClientConfigManager.getConfig().maxSizeChatMessageList;
    }
}
