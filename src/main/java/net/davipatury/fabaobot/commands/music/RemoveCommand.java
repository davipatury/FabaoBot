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
public class RemoveCommand extends Command {

    public RemoveCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        GuildMusicManager musicManager = bot.getPlayerController().getGuildAudioPlayer(event.getGuild());
        
        if(params.length > 0) {
            List<AudioTrack> queue = musicManager.scheduler.getQueueList();
            if(queue.size() >= 1) {
                try {
                    int position = Integer.parseInt(params[0]);
                    IntStream.range(0, queue.size())
                        .forEach(index -> {
                            if(index + 1 == position) {
                                final EmbedBuilder ebuilder = new EmbedBuilder();
                                ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
                                musicManager.scheduler.getQueue().remove(queue.get(index));
                                ebuilder.setDescription("Música na posição **" + position + "** removida!");
                                event.getChannel().sendMessage(ebuilder.build()).queue();
                            }
                        }
                    );
                } catch(NumberFormatException ex) {}
            }
        }
    }
	
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"rm"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return parameters.length >= 1;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Remove uma música na posição desejada.", false);
        ebuilder.addField("Exemplo", "rm 1", false);
    }
    
}
