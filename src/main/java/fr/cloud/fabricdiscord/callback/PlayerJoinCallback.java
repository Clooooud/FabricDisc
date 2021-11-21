package fr.cloud.fabricdiscord.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerJoinCallback {

    Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinCallback.class, (listeners) -> (sender) -> {
        for (PlayerJoinCallback listener : listeners) {
            listener.join(sender);
        }
    });

    void join(ServerPlayerEntity sender);

}
