/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.misc;

import java.time.temporal.ChronoUnit;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class PingCommand extends Command {

    public PingCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        Message oldMessage = event.getMessage();
        event.getChannel().sendMessage("Ping: ...").queue(newMessage -> {
            newMessage.editMessage("Ping: *" + oldMessage.getCreationTime().until(newMessage.getCreationTime(), ChronoUnit.MILLIS) + "ms*").queue();
        });
    }
	
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"pang", "peng", "pong", "pung"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "PONG!", false);
        ebuilder.addField("Exemplo", "ping", false);
    }
}
