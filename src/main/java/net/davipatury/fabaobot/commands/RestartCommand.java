package net.davipatury.fabaobot.commands;

import net.davipatury.fabaobot.FabaoBot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 *
 * @author Davi
 */
public class RestartCommand extends Command {
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        event.getChannel().sendMessage("Restarting...").queue();
        bot.getJDA().shutdown(true);
        System.exit(3);
    }
	
    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
}
