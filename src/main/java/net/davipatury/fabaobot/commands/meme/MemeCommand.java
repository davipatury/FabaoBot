/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.meme;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.MemeController;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class MemeCommand extends Command {
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        MessageChannel channel = event.getChannel();
        MemeController memeController = bot.getMemeController();
        if(params.length > 0) {
            String memeName = FabaoUtils.arrayToString(params).toLowerCase();
            JSONObject meme = memeController.getMeme(memeName);
            if(meme != null) {
                channel.sendFile(Base64.decode(meme.getString("base64")), ".jpg", null).queue();
            } else {
                channel.sendMessage("\uD83D\uDC40 Não encontrei esse meme.").queue();
            }
        } else {
            EmbedBuilder ebuilder = new EmbedBuilder();
            
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            ebuilder.setTimestamp(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.of("-03:00")));
            
            ebuilder.addField("Total de memes", String.valueOf(memeController.getMemeList().keySet().size()), true);
            
            String memeList = String.join("\n", memeController.getMemeList().keySet().stream().sorted((m1, m2) -> m1.compareTo(m2)).collect(Collectors.toList()));
            HttpResponse<String> response = createPost(memeList, bot.getConfiguration().getCategory("memes").getString("pastebin_key"));
		
            if(response.getStatus() == 200) {
                ebuilder.addField("Lista de memes", "[Clique aqui!](" + response.getBody() + ")", true);
            } else {
		ebuilder.addField("Lista de memes", "Atualmente indisponível. \uD83D\uDE2D", true);
            }
            
            channel.sendMessage(ebuilder.build()).queue();
        }
    }
	
    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"memes"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    private HttpResponse<String> createPost(String content, String pastebin_key) {
        try {
            return Unirest.post("http://pastebin.com/api/api_post.php")
                    .field("api_option", "paste")
                    .field("api_dev_key", pastebin_key)
                    .field("api_paste_code", content).asString();
        } catch (UnirestException ex) {
            return null;
        }
    }
}
