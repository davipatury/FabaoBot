/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands;

import net.davipatury.fabaobot.FabaoBot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class UpdateCommand extends Command {
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        String ownerId = bot.getConfiguration().getCategory("bot").getString("owner_id");
        if(ownerId != null && event.getAuthor().getId().equalsIgnoreCase(ownerId)) {
            String currentVersion = getClass().getPackage().getImplementationVersion();
            event.getChannel().sendMessage(currentVersion).queue();
        }
    }
	
    @Override
    public String getName() {
        return "update";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"atualizar"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
}
