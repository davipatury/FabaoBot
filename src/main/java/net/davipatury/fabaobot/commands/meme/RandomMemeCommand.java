/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.meme;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.MemeController;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class RandomMemeCommand extends Command {

    public RandomMemeCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        MemeController memeController = bot.getMemeController();
        List<String> memeList = memeController.getMemeList().keySet().stream().collect(Collectors.toList());
        String memeName = memeList.get(new Random().nextInt(memeList.size()));
        MessageBuilder mbuilder = new MessageBuilder();
        mbuilder.append("**").append(memeName).append("**");
        event.getChannel().sendFile(Base64.decode(memeController.getMeme(memeName).getString("base64")), ".jpg", mbuilder.build()).queue();
    }
	
    @Override
    public String getName() {
        return "randommeme";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"randomm", "randomeme"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra um meme aleatório.", false);
        ebuilder.addField("Exemplo", "randommeme", false);
    }
    
}
