package net.davipatury.fabaobot.listeners;

import java.util.Arrays;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.controllers.CommandController;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Davi
 */
public class MessageListener extends ListenerAdapter {
    
    private FabaoBot bot;
    
    public MessageListener(FabaoBot bot) {
        this.bot = bot;
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        bot.getStatistics().increaseMessagesInSession(1);
        
        Message message = event.getMessage();
        String prefix = bot.getConfiguration().getCategory("bot").getString("prefix");
        
        if (message.getContent().startsWith(prefix) && message.getContent().length() >= 1 && !message.getAuthor().isBot()) {
            String commandName = message.getContent().substring(prefix.length()).split("\\s")[0].toLowerCase();
            String[] parameters = message.getContent().substring(prefix.length() + commandName.length()).split("\\s");
            
            CommandController bc = bot.getCommandController();
            Command command = bc.getCommand(commandName, true);
            if(command != null) {
                if(!event.isFromType(ChannelType.GROUP) || command.getPermissions().length > 0 || event.getMember().hasPermission(command.getPermissions())) {
                    bc.processCommand(command, parameters, event);
                }
            }
	}
    }
}
