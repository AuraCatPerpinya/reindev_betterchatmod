package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.BetterChatModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.src.game.entity.player.EntityPlayer;
import net.minecraft.src.game.level.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Runnable {
    @Inject(method = "changeWorld", at = @At(value = "HEAD"))
    public void onChangeWorld(World world, String arg2, EntityPlayer player, CallbackInfo ci) {
        if (world == null) {
            BetterChatModClient.getMessageHistory().filterMessageHistory();
        }
    }
}
