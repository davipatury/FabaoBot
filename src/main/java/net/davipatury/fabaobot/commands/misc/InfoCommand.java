/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.misc;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class InfoCommand extends Command {

    public InfoCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
        
        String username;
        
        if(event.isFromType(ChannelType.PRIVATE)) {
            username = event.getJDA().getSelfUser().getName();
        } else {
            username = event.getGuild().getSelfMember().getEffectiveName();
        }
        
        ebuilder.setTitle(username + " Statistics", null);
        
        ebuilder.addField("Guildas", String.valueOf(event.getJDA().getGuilds().size()), true);
        ebuilder.addField("Canais de texto", String.valueOf(event.getJDA().getGuilds().stream().mapToInt(g -> g.getTextChannels().size()).sum()), true);
        ebuilder.addField("Canais de voz", String.valueOf(event.getJDA().getGuilds().stream().mapToInt(g -> g.getVoiceChannels().size()).sum()), true);
        
        ebuilder.addField("Mensagens nesta sessão", String.valueOf(bot.getStatistics().messagesInSession()), true);
        ebuilder.addBlankField(true);
        ebuilder.addField("Comandos nesta sessão", String.valueOf(bot.getStatistics().commandsInSession()), true);
        
        ebuilder.addField("Uptime", getUptimeInfo(), false);
        
        
        ebuilder.addField("Memória", getMemoryInfo(), true);
        ebuilder.addField("Música", getMusicInfo(bot), true);
        ebuilder.addField("Threads", NumberFormat.getIntegerInstance().format(Thread.getAllStackTraces().size()), true);
        
        event.getChannel().sendMessage(ebuilder.build()).queue();
    }
	
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"i"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }

    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra informações sobre o bot.", false);
        ebuilder.addField("Exemplo", "info", false);
    }
    
    private String getUptimeInfo() {
        final long duration = ManagementFactory.getRuntimeMXBean().getUptime();
        
        StringBuilder sb = new StringBuilder();
        final long months = duration / 2592000000L % 12;
        final long days = duration / 86400000L % 30;
        final long hours = duration / 3600000L % 24;
        final long minutes = duration / 60000L % 60;
        final long seconds = duration / 1000L % 60;
        if(months > 0) {
            sb.append(months);
            sb.append(months == 1 ? " mês, " : " meses, ");
        }
        if(days > 0) {
            sb.append(days);
            sb.append(days == 1 ? " dia, " : " dias, ");
        }
        if(hours > 0) {
            sb.append(hours);
            sb.append(hours == 1 ? " hora, " : " horas, ");
        }
        if(minutes > 0) {
            sb.append(minutes);
            sb.append(minutes == 1 ? " minuto, " : " minutos, ");
        }
        if(seconds > 0) {
            sb.append(seconds);
            sb.append(seconds == 1 ? " segundo" : " segundos");
        }
        return sb.toString();
    }
    
    private String getMemoryInfo() {
        NumberFormat f = new DecimalFormat("###,##0.0");
        StringBuilder sb = new StringBuilder();
        sb.append("**Uso**: `").append(f.format(Math.round((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024 * 1024)))).append("MB`\n");
        sb.append("**Total**: `").append(f.format(Math.round(Runtime.getRuntime().totalMemory()/(1024 * 1024)))).append("MB`\n");
        sb.append("**Máximo**: `").append(f.format(Math.round(Runtime.getRuntime().maxMemory()/(1024 * 1024)))).append("MB`");
        return sb.toString();
    }
    
    private String getMusicInfo(FabaoBot bot) {
        StringBuilder sb = new StringBuilder();
        sb.append("**Conexões:** `");
        sb.append(bot.getJDA().getGuilds().stream().map(g -> g.getAudioManager()).filter(am -> am.getConnectionStatus().equals(ConnectionStatus.CONNECTED)).count()).append("`\n");
        sb.append("**Queue size:** `");
        sb.append(bot.getJDA().getGuilds().stream().mapToInt(g -> bot.getPlayerController().getGuildAudioPlayer(g).scheduler.getQueueList().size()).sum()).append("`");
        return sb.toString();
    }

}
