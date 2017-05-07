/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
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
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        try {
            JSONObject newPom = XML.toJSONObject(IOUtils.toString(new URL("https://raw.githubusercontent.com/davipatury/FabaoBot/master/pom.xml"), "UTF-8"));
            StringBuilder versionInfo = new StringBuilder();
            
            double currentVersion = Double.parseDouble(getClass().getPackage().getImplementationVersion());
            double newestVersion = newPom.getJSONObject("project").getDouble("version");
            versionInfo.append("**Current version:** ").append(FabaoUtils.formatVersion(currentVersion)).append("\n")
                       .append("**Newest version:** ").append(FabaoUtils.formatVersion(newestVersion)).append("\n")
                       .append("Checking for updates...");
                
            Message message = event.getChannel().sendMessage(versionInfo.toString()).complete();
                
            if(newestVersion > currentVersion) {
                message.editMessage(versionInfo.delete(versionInfo.length() - 23, versionInfo.length()) + "Downloading...").queue();
                File newJar = new File("FabaoBot-updated.jar");
                FileUtils.copyURLToFile(new URL("https://github.com/davipatury/FabaoBot/releases/download/v"+FabaoUtils.formatVersion(newestVersion)+"/FabaoBot.jar"), newJar);
                message.editMessage(versionInfo.delete(versionInfo.length() - 23, versionInfo.length()) + "Restarting!").queue(msg -> {
                    bot.getJDA().shutdown(true);
                    System.exit(3);
                });
            } else {
                message.editMessage(versionInfo.delete(versionInfo.length() - 23, versionInfo.length()) + "You are using an updated version!").queue();
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
    
}
