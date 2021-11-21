package fr.cloud.fabricdiscord;

import fr.cloud.fabricdiscord.callback.PlayerChatCallback;
import fr.cloud.fabricdiscord.callback.PlayerJoinCallback;
import fr.cloud.fabricdiscord.callback.PlayerLeaveCallback;
import fr.cloud.fabricdiscord.discord.DiscordBot;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class FabricDiscord implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("fabricdiscord");
    private DiscordBot discordBot;
    private MinecraftServer server;

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);

        Version version = FabricLoaderImpl.INSTANCE.getModContainer("fabricdiscord").get().getMetadata().getVersion();
        LOGGER.info("FabricDiscord v" + version.getFriendlyString() + " starting");
        LOGGER.info("Launching Discord Server...");

        discordBot = new DiscordBot(this);
        discordBot.start();

        LOGGER.info("Registering events");

        PlayerChatCallback.EVENT.register((sender, content) -> discordBot.sendMessage(sender.getEntityName() + ": " + content));
        PlayerJoinCallback.EVENT.register(player -> discordBot.sendMessage(player.getEntityName() + " has joined the server."));
        PlayerLeaveCallback.EVENT.register(player -> discordBot.sendMessage(player.getEntityName() + " has left the server."));

        LOGGER.info("Event registered");
        LOGGER.info("FabricDiscord v" + version.getFriendlyString() + " started");
    }

    private void onServerStarted(MinecraftServer minecraftServer) {
        server = minecraftServer;
    }
}
