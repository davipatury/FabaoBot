package net.davipatury.fabaobot.commands.meme;

import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.MemeController;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class RemoveMemeCommand extends Command {

    public RemoveMemeCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        final String word = FabaoUtils.arrayToString(params).toLowerCase();
        final MemeController memeController = bot.getMemeController();
        final EmbedBuilder ebuilder = new EmbedBuilder();
				
        if(memeController.hasMeme(word)) {
            final Message message = event.getMessage();
            
            memeController.removeMeme(word);
            memeController.save();
            
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            ebuilder.setDescription("\u2705 Meme **"+word+"** removido!");
            ebuilder.setFooter("Removido por " + FabaoUtils.formatUsername(message.getAuthor()), message.getAuthor().getEffectiveAvatarUrl());
            FabaoUtils.safeDeleteMessage(message);
            
            event.getChannel().sendMessage(ebuilder.build()).queue();
        } else {
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            ebuilder.setDescription("\u2757 Esse meme não existe!");
            event.getChannel().sendMessage(ebuilder.build()).queue();
        }
    }
	
    @Override
    public String getName() {
        return "removememe";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"rmeme", "memeremove"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return parameters.length >= 1;
    }
    
}
