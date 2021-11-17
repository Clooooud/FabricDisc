package fr.cloud.fabricdiscord.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerChatCallback {

    Event<PlayerChatCallback> EVENT = EventFactory.createArrayBacked(PlayerChatCallback.class, (listeners) -> (sender, content) -> {
        for (PlayerChatCallback listener : listeners) {
            listener.sendMessage(sender, content);
        }
    });

    void sendMessage(ServerPlayerEntity sender, String content);

}
