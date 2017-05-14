/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.misc;

import com.sun.xml.internal.ws.util.StringUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class ProfileCommand extends Command {

    public ProfileCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        Message message = event.getMessage();
	Guild guild = event.getGuild();
	EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(FabaoUtils.getColor(guild));
        
	final Member member;
        if(params.length > 0) {	
            if(!message.getMentionedUsers().isEmpty()) {
                member = guild.getMember(message.getMentionedUsers().get(0));
            } else {
                String query = FabaoUtils.arrayToString(params);
                if(!guild.getMembersByNickname(query, true).isEmpty()) {
                    member = guild.getMembersByNickname(query, true).get(0);
                } else if(!guild.getMembersByName(query, true).isEmpty()) {
                    member = guild.getMembersByName(query, true).get(0);
                } else {
                    ebuilder.setDescription("\u2757 Não encontrei ninguém com esse nome.");
                    event.getChannel().sendMessage(ebuilder.build()).queue();
                    return;
                }
            }
        } else {
            member = event.getMember();
        }

        if(member != null) {
            ebuilder.setTitle("Informação sobre " + member.getUser().getName() + " (" + member.getUser().getId() + ")", null);
            ebuilder.setColor(member.getColor());
            ebuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());

            ebuilder.addField("Status", StringUtils.capitalize(member.getOnlineStatus().toString().toLowerCase()), true);

            if(member.getGame() != null) {
                ebuilder.addField("Jogando", member.getGame().getName(), true);
            }

            LocalDateTime joinTime = LocalDateTime.ofInstant(member.getJoinDate().toInstant(), ZoneId.systemDefault());
            LocalDateTime createTime = LocalDateTime.ofInstant(member.getUser().getCreationTime().toInstant(), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' YYYY");

            ebuilder.addField("Conta criada em", createTime.format(formatter), true);

            List<Member> sortedMembers = event.getGuild().getMembers().stream().sorted((m1, m2) -> m1.getJoinDate().compareTo(m2.getJoinDate())).collect(Collectors.toList());
            ebuilder.addField("Membro número", String.valueOf(sortedMembers.indexOf(member) + 1), true);
            
            ebuilder.addField("Juntou-se em", joinTime.format(formatter), true);

            List<String> joinOrder = new ArrayList<>();
            sortedMembers.stream().filter((m) -> sortedMembers.indexOf(m) - sortedMembers.indexOf(member) < 3 && sortedMembers.indexOf(m) - sortedMembers.indexOf(member) > -3).forEach((m) -> {
                if(m == member) {
                    joinOrder.add("**" + m.getEffectiveName() + "**");
                } else {
                    joinOrder.add(m.getEffectiveName());
                }
            });

            ebuilder.addField("Ordem de chegada", String.join(" -> ", joinOrder), false);

            if(!member.getRoles().isEmpty()) {
                ebuilder.addField("Cargos", String.join(", ", member.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList())), false);
            }
        }
        
	event.getChannel().sendMessage(ebuilder.build()).queue();
    }
	
    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"pl"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }

    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra informações sobre um membro da guilda.", false);
        ebuilder.addField("Exemplo", "profile <nome do membro>", false);
    }
    
}
