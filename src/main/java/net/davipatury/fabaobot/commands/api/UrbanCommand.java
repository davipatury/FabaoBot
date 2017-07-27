/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class UrbanCommand extends Command {

    public UrbanCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
        String query = FabaoUtils.arrayToString(params, 0, "+");
        
        try {
            HttpResponse<JsonNode> response = Unirest.get("http://api.urbandictionary.com/v0/define?term=" + query)
                .asJson();
            
            if(response.getStatus() == 200) {
                Message message = event.getMessage();
                JSONObject json = response.getBody().getObject();
                
                if(json.getJSONArray("list").length() > 0) {
                    ebuilder.setTitle(json.getJSONArray("list").getJSONObject(0).getString("word"), null);
                    ebuilder.setAuthor("Pedido por " + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator(), null, message.getAuthor().getEffectiveAvatarUrl());
                    ebuilder.setFooter("Autor: " + json.getJSONArray("list").getJSONObject(0).getString("author"), null);

                    if(json.getJSONArray("list").getJSONObject(0).getString("definition").length() > MessageEmbed.VALUE_MAX_LENGTH) {
                        ebuilder.addField("Definição", json.getJSONArray("list").getJSONObject(0).getString("definition").substring(0, 1021) + "...", true);
                    } else {
                        ebuilder.addField("Definição", json.getJSONArray("list").getJSONObject(0).getString("definition"), true);
                    }
                    ebuilder.addField("Exemplo", json.getJSONArray("list").getJSONObject(0).getString("example"), false);
                    ebuilder.addField("Upvotes", String.valueOf(json.getJSONArray("list").getJSONObject(0).getInt("thumbs_up")) + "\uD83D\uDC4D", true);
                    ebuilder.addField("Downvotes", String.valueOf(json.getJSONArray("list").getJSONObject(0).getInt("thumbs_down")) + "\uD83D\uDC4E", true);
                } else {
                    ebuilder.setDescription("\u2757 Não encontrei nada.");
                }
            } else {
                ebuilder.setDescription("\u2757 Não encontrei nada.");
            }
	} catch(UnirestException e) {
            ebuilder.setDescription("\u2757 Não encontrei nada.");
        }
        
        event.getChannel().sendMessage(ebuilder.build()).queue();
    }
	
    @Override
    public String getName() {
        return "urban";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"ur", "ub"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return parameters.length >= 1;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra o significado de uma palavra de acordo com o Urban Dictionary.", false);
        ebuilder.addField("Exemplo", "urban Brasil", false);
    }
    
}
