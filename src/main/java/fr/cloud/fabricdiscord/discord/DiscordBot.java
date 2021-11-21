package fr.cloud.fabricdiscord.discord;

import fr.cloud.fabricdiscord.FabricDiscord;
import fr.cloud.fabricdiscord.config.Configuration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class DiscordBot extends Thread {

    private final FabricDiscord fabricDiscord;

    private Configuration configuration;
    private JDA jda;
    private TextChannel textChannel;

    private final Queue<Runnable> actionQueue = new ArrayDeque<>();

    @Override
    public void run() {
        configuration = new Configuration();
        configuration.loadConfig();

        try {
            jda = JDABuilder.createLight(configuration.getToken(), GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(new ChatListener(this))
                    .build();
            jda.awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }

        textChannel = getChannel();

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getChannel().getManager().setTopic(getUpdatedChannelDesc()).queue();
            }
        }, 1000L * 15L, 1000L * 15L);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Runnable runnable;
                while ((runnable = actionQueue.poll()) != null) {
                    runnable.run();
                }
            }
        }, 1000, 1000);
    }

    public void sendMessage(String message) {
        actionQueue.add(() -> this.getChannel().sendMessage(message).queue());
    }

    private String getUpdatedChannelDesc() {
        if (getFabricDiscord().getServer() == null) {
            return "";
        }

        PlayerManager playerManager = getFabricDiscord().getServer().getPlayerManager();

        int numberOfPlayers = playerManager.getCurrentPlayerCount();
        int maxPlayers = playerManager.getMaxPlayerCount();
        String players = playerManager.getPlayerList().stream().map(ServerPlayerEntity::getEntityName).collect(Collectors.joining(", "));

        return "There are " + numberOfPlayers + "/" + maxPlayers + " players online | " + players;
    }

    @SneakyThrows
    public TextChannel getChannel() {
        Guild guild = Optional.ofNullable(jda.getGuildById(Long.parseLong(configuration.getServerId()))).orElseThrow((Supplier<Throwable>) () -> {
            jda.shutdownNow();
            return new NoSuchElementException();
        });

        return Optional.ofNullable(((TextChannel) guild.getGuildChannelById(Long.parseLong(configuration.getChannelId())))).orElseThrow((Supplier<Throwable>) () -> {
            jda.shutdownNow();
            return new NoSuchElementException();
        });
    }
}
