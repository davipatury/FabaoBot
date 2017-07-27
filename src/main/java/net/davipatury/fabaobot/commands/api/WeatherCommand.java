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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class WeatherCommand extends Command {

    public WeatherCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
        String query = FabaoUtils.arrayToString(params, 0, "+");
        
        try {
            HttpResponse<JsonNode> response = Unirest.get("http://api.openweathermap.org/data/2.5/weather?q=" + query + "&lang=pt&type=like&units=metric&APPID=" + bot.getConfiguration().getCategory("apis").getString("openweather_appid"))
                .asJson();
            
            if(response.getStatus() == 200) {
                Message message = event.getMessage();
                JSONObject json = response.getBody().getObject();
                
                ebuilder.setTitle(json.getString("name") + ", " + json.getJSONObject("sys").getString("country"), null);
                ebuilder.setThumbnail("https://openweathermap.org/img/w/" + json.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png");
                ebuilder.setAuthor("Pedido por " + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator(), null, message.getAuthor().getEffectiveAvatarUrl());

                int now = (int) (System.currentTimeMillis() / 1000L);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));

                String sunrise = sdf.format(new Date((long) (json.getJSONObject("sys").getDouble("sunrise")*1000L)));
                String sunset = sdf.format(new Date((long) (json.getJSONObject("sys").getDouble("sunset")*1000L)));

                sdf.setTimeZone(TimeZone.getTimeZone("GMT-0"));

                String untilSunR = sdf.format(new Date((long) (json.getJSONObject("sys").getDouble("sunrise") - now)*1000L));
                String untilSunS = sdf.format(new Date((long) (json.getJSONObject("sys").getDouble("sunset") - now)*1000L));

                ebuilder.addField("País", ":flag_" + json.getJSONObject("sys").getString("country").toLowerCase() + ":", true);
                ebuilder.addField("Clima", json.getJSONArray("weather").getJSONObject(0).getString("main") + ", " + json.getJSONArray("weather").getJSONObject(0).getString("description"), true);
                ebuilder.addField("Temperatura", String.valueOf(Math.round(json.getJSONObject("main").getDouble("temp"))) + "ºC", true);
                ebuilder.addField("Umidade", String.valueOf(Math.round(json.getJSONObject("main").getDouble("humidity"))) + "%", true);
                ebuilder.addField("Nascer do sol", sunrise, true);
                ebuilder.addField("Pôr do sol", sunset, true);
                ebuilder.addField("Tempo até o nascer do sol", untilSunR, true);
                ebuilder.addField("Tempo até o pôr do sol", untilSunS, true);
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
        return "weather";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"clima"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return parameters.length >= 1;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Informações sobre clima.", false);
        ebuilder.addField("Exemplo", "weather Rio de Janeiro", false);
    }
}
