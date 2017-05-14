package net.davipatury.fabaobot.commands.admin;

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
public class RestartCommand extends Command {

    public RestartCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        event.getChannel().sendMessage("Restarting...").queue(msg -> {
            bot.shutdown(2);
        });
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
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Use-o para reiniciar este bot.", false);
        ebuilder.addField("Exemplo", "restart", false);
    }
}
