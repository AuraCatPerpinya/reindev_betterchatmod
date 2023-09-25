package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.IWithPastSentMessages;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Runnable, IWithPastSentMessages {
    @Inject(method = "startGame", at = @At(value = "RETURN"))
    public void startGameMixin(CallbackInfo ci) {

    }
}
