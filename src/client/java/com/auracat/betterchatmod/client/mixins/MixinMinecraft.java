package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.IWithChatInputLogs;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Runnable, IWithChatInputLogs {
}
