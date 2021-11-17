package fr.cloud.fabricdiscord;

import fr.cloud.fabricdiscord.config.Configuration;
import fr.cloud.fabricdiscord.discord.ChatListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class DiscordBot extends Thread implements Runnable {

    private final FabricDiscord fabricDiscord;

    private Configuration configuration;
    private JDA jda;
    private TextChannel textChannel;

    @Override
    @SneakyThrows
    public void run() {
        configuration = new Configuration();
        configuration.loadConfig();

        jda = JDABuilder.createDefault(configuration.getToken())
                .addEventListeners(new ChatListener(this))
                .build();
        jda.awaitReady();
        textChannel = getChannel();
    }

    @SneakyThrows
    public TextChannel getChannel() {
        Guild guild = Optional.of(jda.getGuildById(Long.parseLong(configuration.getServerId()))).orElseThrow((Supplier<Throwable>) () -> {
            jda.shutdownNow();
            return new NoSuchElementException();
        });

        return Optional.of(((TextChannel) guild.getGuildChannelById(Long.parseLong(configuration.getChannelId())))).orElseThrow((Supplier<Throwable>) () -> {
            jda.shutdownNow();
            return new NoSuchElementException();
        });
    }
}
