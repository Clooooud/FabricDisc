package fr.cloud.fabricdiscord.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ChatListener extends ListenerAdapter {

    private final DiscordBot bot;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (!event.isFromGuild())
            return;

        String formattedText = "§8[§9Discord§8] §r" + event.getAuthor().getAsTag() + "§7: §r" + event.getMessage().getContentRaw();
        bot.getFabricDiscord().getServer().getPlayerManager().broadcastChatMessage(Text.of(formattedText), MessageType.SYSTEM, Util.NIL_UUID);
    }
}
