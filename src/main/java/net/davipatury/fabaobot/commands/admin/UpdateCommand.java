/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.admin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Davi
 */
public class UpdateCommand extends Command {

    public UpdateCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        try {
            JSONObject newPom = XML.toJSONObject(IOUtils.toString(new URL("https://raw.githubusercontent.com/davipatury/FabaoBot/master/pom.xml"), "UTF-8"));
            EmbedBuilder ebuilder = new EmbedBuilder();
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            
            String avatarUrl = bot.getJDA().getSelfUser().getEffectiveAvatarUrl();
            
            double currentVersion = Double.parseDouble(getClass().getPackage().getImplementationVersion());
            double newestVersion = newPom.getJSONObject("project").getDouble("version");
            
            ebuilder.addField("Current version", FabaoUtils.formatVersion(currentVersion), true);
            ebuilder.addField("Newest version", FabaoUtils.formatVersion(newestVersion), true);
            ebuilder.setFooter("Checking for updates...", avatarUrl);
            
            Message versionInfo = event.getChannel().sendMessage(ebuilder.build()).complete();
            
            if(newestVersion > currentVersion) {
                ebuilder.setFooter("Downloading...", avatarUrl);
                versionInfo.editMessage(ebuilder.build()).queue();
                
                File newJar = new File("FabaoBot-updated.jar");
                FileUtils.copyURLToFile(new URL("https://github.com/davipatury/FabaoBot/releases/download/v"+FabaoUtils.formatVersion(newestVersion)+"/FabaoBot.jar"), newJar);
                
                ebuilder.setFooter("Restarting...", avatarUrl);
                versionInfo.editMessage(ebuilder.build()).queue(msg -> {
                    bot.shutdown(3);
                });
            } else {
                ebuilder.setFooter("You are using an updated version!", avatarUrl);
                versionInfo.editMessage(ebuilder.build()).queue();
            }
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {}
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
        return new String[]{};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Use-o para atualizar este bot.", false);
        ebuilder.addField("Exemplo", "update", false);
    }
}
