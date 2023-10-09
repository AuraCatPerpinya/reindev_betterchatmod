package com.auracat.betterchatmod.client.mixins;

import com.auracat.betterchatmod.client.BetterChatModClient;
import com.auracat.betterchatmod.client.config.ClientConfigManager;
import com.auracat.betterchatmod.client.messagehistory.MessageHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.game.entity.player.EntityPlayer;
import net.minecraft.src.game.level.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Runnable {
    @Shadow public abstract boolean lineIsCommand(String arg1);

    @Inject(method = "startGame", at = @At(value = "RETURN"))
    public void onStartGame(CallbackInfo ci) {
    }

    @Inject(method = "changeWorld", at = @At(value = "HEAD"))
    public void onChangeWorld(World world, String arg2, EntityPlayer player, CallbackInfo ci) {
        if (world == null) {
            MessageHistory messageHistory = BetterChatModClient.getMessageHistory();
            List<String> msgHistoryList = messageHistory.getList();

            assert ClientConfigManager.getConfig() != null;
            switch (ClientConfigManager.getConfig().messageHistoryIsPerWorld) {
                case NO:
                    break;
                case ONLY_COMMANDS:
                    List<String> collectOnlyCommands = msgHistoryList
                            .stream()
                            .filter(this::lineIsCommand)
                            .collect(Collectors.toList());
                    messageHistory.setList(collectOnlyCommands);
                    break;
                case ONLY_NORMAL_MSGS:
                    List<String> collectOnlyNormalMsgs = messageHistory.getList()
                            .stream()
                            .filter((message) -> !(this.lineIsCommand(message)))
                            .collect(Collectors.toList());
                    messageHistory.setList(collectOnlyNormalMsgs);
                    break;
                case YES:
                    msgHistoryList.clear();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + ClientConfigManager.getConfig().messageHistoryIsPerWorld);
            }
        }
    }
}
