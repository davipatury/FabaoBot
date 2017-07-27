/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.misc;

import com.sun.xml.internal.ws.util.StringUtils;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class GuildInfoCommand extends Command {

    public GuildInfoCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        Guild guild = event.getGuild();
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(FabaoUtils.getColor(guild));
        
	if(guild.getIconUrl() != null) {
            ebuilder.setThumbnail(guild.getIconUrl());
        } else {
            ebuilder.setThumbnail("https://discordapp.com/assets/dd4dbc0016779df1378e7812eabaa04d.png");
        }
        
        ebuilder.addField("Nome do servidor", guild.getName(), true);
        ebuilder.addField("ID do servidor", guild.getId(), true);
        ebuilder.addField("Nome do dono", guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), true);
        ebuilder.addField("ID do dono", guild.getOwner().getUser().getId(), true);
        ebuilder.addField("Canais de texto", String.valueOf(guild.getTextChannels().size()), true);
        ebuilder.addField("Canais de voz", String.valueOf(guild.getVoiceChannels().size()), true);
        ebuilder.addField("Membros", String.valueOf(guild.getMembers().size()), true);
        ebuilder.addField("Nível de verificação", StringUtils.capitalize(guild.getVerificationLevel().toString().toLowerCase()), true);
        ebuilder.addField("Cargos", String.valueOf(guild.getRoles().size()), true);
	ebuilder.addField("Região", guild.getRegion().getName(), true);
        ebuilder.addField("Criação", guild.getCreationTime().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' YYYY")), true);
        ebuilder.addField("Emotes", String.valueOf(guild.getEmotes().size()), true);
        ebuilder.addField("Lista de emotes", String.join(" ", guild.getEmotes().stream().map(emote -> emote.getAsMention()).collect(Collectors.toList())), false);
        
	event.getChannel().sendMessage(ebuilder.build()).queue();
    }
	
    @Override
    public String getName() {
        return "guildinfo";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"gi"};
    }
    
    @Override
    public boolean canProcess(final MessageReceivedEvent event, final String[] parameters, final FabaoBot bot) {
        return event.getGuild() != null && verifyParameters(parameters);
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }

    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra informações sobre a guilda atual.", false);
        ebuilder.addField("Exemplo", "guildinfo", false);
    }
}
