package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.messagehistory.IWithMessageHistory;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Runnable, IWithMessageHistory {
    @Inject(method = "startGame", at = @At(value = "RETURN"))
    public void onStartGame(CallbackInfo ci) {
    }
}
