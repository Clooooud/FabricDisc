package fr.cloud.fabricdiscord.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerLeaveCallback {

    Event<PlayerLeaveCallback> EVENT = EventFactory.createArrayBacked(PlayerLeaveCallback.class, (listeners) -> (sender) -> {
        for (PlayerLeaveCallback listener : listeners) {
            listener.join(sender);
        }
    });

    void join(ServerPlayerEntity sender);
}
