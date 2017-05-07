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
    
    private final FabaoBot bot;
    
    public MessageListener(FabaoBot bot) {
        this.bot = bot;
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        bot.getStatistics().increaseMessagesInSession(1);
        
        Message message = event.getMessage();
        String prefix = bot.getConfiguration().getCategory("bot").getString("prefix");
        
        if (message.getContent().startsWith(prefix) && message.getContent().length() >= 1 && !message.getAuthor().isBot()) {
            String[] fullCommand = message.getContent().substring(prefix.length()).split("\\s");
            String commandName = fullCommand[0].toLowerCase();
            String[] parameters = Arrays.copyOfRange(fullCommand, 1, fullCommand.length);
            
            CommandController commandController = bot.getCommandController();
            Command command = commandController.getCommand(commandName, true);
            if(command != null) {
                commandController.processCommand(command, parameters, event);
            }
	}
    }
}
