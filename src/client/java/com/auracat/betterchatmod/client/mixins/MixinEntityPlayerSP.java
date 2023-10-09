package com.auracat.betterchatmod.client.mixins;

import net.minecraft.src.client.player.EntityPlayerSP;
import net.minecraft.src.game.entity.player.EntityPlayer;
import net.minecraft.src.game.level.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityPlayer {
    public MixinEntityPlayerSP(World world) {
        super(world);
    }

    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"))
    public void onSendChatMessage(String s, CallbackInfo ci) {
    }
}
