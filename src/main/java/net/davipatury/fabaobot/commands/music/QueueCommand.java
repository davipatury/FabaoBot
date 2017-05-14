/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.stream.IntStream;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.davipatury.fabaobot.player.GuildMusicManager;
import net.davipatury.fabaobot.player.MusicUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class QueueCommand extends Command {

    public QueueCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        GuildMusicManager musicManager = bot.getPlayerController().getGuildAudioPlayer(event.getGuild());
        
        AudioTrack playingTrack = musicManager.player.getPlayingTrack();
        if(playingTrack != null) {
            final EmbedBuilder ebuilder = new EmbedBuilder();
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            ebuilder.setTitle("Fila de reprodução", null);
            
            ebuilder.addField("Tocando agora", "[" + playingTrack.getInfo().title + "](" + playingTrack.getInfo().uri + ")", false);
            
            List<AudioTrack> queue = musicManager.scheduler.getQueueList();
            if(queue.size() >= 1) {
                StringBuilder queueBuilder = new StringBuilder();
                IntStream.range(0, queue.size())
                    .forEach(index -> {
                        AudioTrack track = queue.get(index);
                        queueBuilder.append(index + 1).append(". [").append(track.getInfo().title).append("](").append(track.getInfo().uri).append(")\n");
                    }
                );
                ebuilder.addField("Lista de reprodução", queueBuilder.toString(), false);
                
                ebuilder.setFooter("Duração total da lista: " + MusicUtils.formatSeconds(queue.stream().mapToLong(track -> track.getDuration()).sum()), null);
            }
            
            event.getChannel().sendMessage(ebuilder.build()).queue();
        }
    }
	
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"queue"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra uma lista com as músicas da lista de reprodução atual.", false);
        ebuilder.addField("Exemplo", "queue", false);
    }
}
