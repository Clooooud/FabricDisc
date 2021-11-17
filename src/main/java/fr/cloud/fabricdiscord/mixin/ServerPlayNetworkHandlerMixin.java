package fr.cloud.fabricdiscord.mixin;

import fr.cloud.fabricdiscord.callback.PlayerChatCallback;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(at = @At(value = "TAIL"), method = "onGameMessage")
    public void callEvent(ChatMessageC2SPacket packet, CallbackInfo callbackInfo) {
        String chatMessage = packet.getChatMessage();
        if (chatMessage.startsWith("/")){
            return;
        }
        PlayerChatCallback.EVENT.invoker().sendMessage(((ServerPlayNetworkHandler)(Object)this).getPlayer(), chatMessage);
    }
}
