package fr.cloud.fabricdiscord.mixin;

import fr.cloud.fabricdiscord.callback.PlayerChatCallback;
import fr.cloud.fabricdiscord.callback.PlayerJoinCallback;
import fr.cloud.fabricdiscord.callback.PlayerLeaveCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(at = @At(value = "TAIL"), method = "onGameMessage")
    public void callChatEvent(ChatMessageC2SPacket packet, CallbackInfo callbackInfo) {
        String chatMessage = packet.getChatMessage();
        if (chatMessage.startsWith("/")){
            return;
        }
        PlayerChatCallback.EVENT.invoker().sendMessage(((ServerPlayNetworkHandler)(Object)this).getPlayer(), chatMessage);
    }

    @Inject(at = @At(value = "TAIL"), method = "<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    public void callConnectionEvent(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, CallbackInfo callbackInfo) {
        PlayerJoinCallback.EVENT.invoker().join(player);
    }

    @Inject(at = @At(value = "HEAD"), method = "onDisconnected")
    public void callDisconnectionEvent(Text reason, CallbackInfo callbackInfo) {
        PlayerLeaveCallback.EVENT.invoker().join(((ServerPlayNetworkHandler)(Object)this).getPlayer());
    }

    @Inject(at = @At(value = "HEAD"), method = "disconnect")
    public void callKickEvent(Text reason, CallbackInfo callbackInfo) {
        PlayerLeaveCallback.EVENT.invoker().join(((ServerPlayNetworkHandler)(Object)this).getPlayer());
    }
}
