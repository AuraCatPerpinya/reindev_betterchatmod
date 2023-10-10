package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.utils.LeavingWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.src.game.entity.player.EntityPlayer;
import net.minecraft.src.game.level.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Runnable {
    @SuppressWarnings("SameReturnValue")
    @Shadow
    public static Minecraft getInstance() {
        return null;
    }

    @Inject(method = "changeWorld", at = @At(value = "HEAD"))
    public void onChangeWorld(World world, String arg2, EntityPlayer player, CallbackInfo ci) {
        Minecraft minecraft = getInstance();
        if (world == null) {
            LeavingWorld.filterMessageHistory(minecraft);
        }
    }
}
