package com.auracat.betterchatmod.client.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.src.client.Session;
import net.minecraft.src.client.player.EntityClientPlayerMP;
import net.minecraft.src.client.player.EntityPlayerSP;
import net.minecraft.src.game.level.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityClientPlayerMP.class)
public abstract class MixinEntityClientPlayerMP extends EntityPlayerSP {
    public MixinEntityClientPlayerMP(Minecraft minecraft, World world, Session session, int arg4) {
        super(minecraft, world, session, arg4);
    }

    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"))
    public void sendChatMessageMixin(String s, CallbackInfo ci) {
   }
}
