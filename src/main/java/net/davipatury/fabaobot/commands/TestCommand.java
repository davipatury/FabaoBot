package net.davipatury.fabaobot.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.controllers.MemeController;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class TestCommand extends Command {
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        String ownerId = bot.getConfiguration().getCategory("bot").getString("owner_id");
        if(ownerId != null && event.getAuthor().getId().equalsIgnoreCase(ownerId)) {
            try {
                JSONObject oldMemes = new JSONObject(Files.lines(Paths.get("old_memes.json")).collect(Collectors.joining())).getJSONObject("memes");
                MemeController memeController = bot.getMemeController();
                oldMemes.keySet().stream().forEach(key -> {
                    JSONObject newMeme = new JSONObject()
                            .accumulate("base64", oldMemes.getString(key));
                    memeController.addMeme(key, newMeme);
                });
                memeController.save();
            } catch (IOException ex) {
                Logger.getLogger(TestCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
	
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"teste"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
}
