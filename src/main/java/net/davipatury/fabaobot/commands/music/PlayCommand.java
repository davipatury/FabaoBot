/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.music;

import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.davipatury.fabaobot.player.PlayerController;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class PlayCommand extends Command {

    public PlayCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        PlayerController playerController = bot.getPlayerController();
        String query = FabaoUtils.arrayToString(params);
        playerController.loadAndPlay(event.getTextChannel(), query, event.getMember());
    }
	
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"tocar"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return parameters.length >= 1;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Adiciona uma música a lista de reprodução atual.", false);
        ebuilder.addField("Exemplo", "play <link>, play <nome da música>", false);
    }
}
