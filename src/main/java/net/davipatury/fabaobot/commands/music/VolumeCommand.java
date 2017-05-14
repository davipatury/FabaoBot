/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class VolumeCommand extends Command {

    public VolumeCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        AudioPlayer player = bot.getPlayerController().getGuildAudioPlayer(event.getGuild()).player;
        
        if(player.getPlayingTrack() != null) {
            try {
                if(params.length >= 1) {
                try {    
                    //int target = Math.min(150, Math.max(Integer.parseInt(params[0]), 0));
                    int target = Integer.parseInt(params[0]);
                    player.setVolume(target);
                    event.getChannel().sendMessage("\u2705 Volume definido para `" + player.getVolume() + "`").queue();
                } catch(NumberFormatException e) {}
            } else {
                event.getChannel().sendMessage("\uD83D\uDD09 Volume atual: `" + player.getVolume() + "`").queue();
            }
            } catch(NumberFormatException ex) {
                event.getChannel().sendMessage("Volume inválido!").queue();
            }
        }
    }
	
    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"vol"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Define o valor do player de música.", false);
        ebuilder.addField("Exemplo", "vol, vol <volume desejado>", false);
    }
}
