package fr.cloud.fabricdiscord.discord;

import fr.cloud.fabricdiscord.DiscordBot;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class ChatListener implements EventListener {

    private final DiscordBot bot;

    // Ecoute pour les events de messages reçu pour exécuter les commandes si ça en est
    public void onEvent(@Nonnull GenericEvent event) {
        if(event instanceof MessageReceivedEvent){
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            // Si c'est en message privé, si l'envoyeur est un bot ou si  le message ne contient pas le préfix, on ne va pas plus loin
            if(e.getAuthor().isBot())
                return;
            if(!e.isFromGuild())
                return;

            String formattedText = "§8[§9Discord§8] §r" + e.getAuthor().getAsTag() + "§7: §r" + e.getMessage().getContentRaw();
            bot.getFabricDiscord().getServer().getPlayerManager().broadcastChatMessage(Text.of(formattedText), MessageType.SYSTEM, Util.NIL_UUID);
        }
    }

}
