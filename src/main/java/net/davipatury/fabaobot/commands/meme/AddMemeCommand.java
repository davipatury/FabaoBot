package net.davipatury.fabaobot.commands.meme;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.MemeController;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class AddMemeCommand extends Command {
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        String possibleUrl = params[0];
        final String word = FabaoUtils.arrayToString(params, 1);
        final MemeController memeController = bot.getMemeController();
        final EmbedBuilder ebuilder = new EmbedBuilder();
				
        if(!memeController.hasMeme(word.toLowerCase())) {
            final Message message = event.getMessage();
            
            String base64 = urlToBase64(possibleUrl);
            if(base64 != null) {
                memeController.addMeme(word, new JSONObject().accumulate("base64", base64));
            } else {
                if(!message.getEmbeds().isEmpty() && message.getEmbeds().get(0).getThumbnail() != null) {
                    possibleUrl = message.getEmbeds().get(0).getThumbnail().getUrl();
                    base64 = urlToBase64(possibleUrl);
                    if(base64 != null) {
                        memeController.addMeme(word, new JSONObject().accumulate("base64", base64));
                    }
                }
            }
            
            memeController.save();
            
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            if(memeController.hasMeme(word)) {
                ebuilder.setDescription("\u2705 Meme **"+word+"** adicionado!");
                ebuilder.setImage(possibleUrl);
                ebuilder.setFooter("Adicionado por " + FabaoUtils.formatUsername(message.getAuthor()), message.getAuthor().getEffectiveAvatarUrl());
                FabaoUtils.safeDeleteMessage(message);
            } else {
                ebuilder.setDescription("\u2757 Um erro ocorreu!");
            }
            
            event.getChannel().sendMessage(ebuilder.build()).queue();
        } else {
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            ebuilder.setDescription("\u2757 Já existe um meme com esse nome!");
            event.getChannel().sendMessage(ebuilder.build()).queue();
        }
    }
	
    @Override
    public String getName() {
        return "addmeme";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"ameme", "memeadd"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return parameters.length >= 2;
    }
    
    private String urlToBase64(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            byte[] buffer = IOUtils.toByteArray(uc);
            return Base64.getEncoder().encodeToString(buffer);
        } catch(IOException e) {
            return null;
        }
    }
}
