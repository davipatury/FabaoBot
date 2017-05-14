/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.music;

import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.text.NumberFormat;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.apis.YoutubeAPI;
import net.davipatury.fabaobot.apis.twitch.TwitchAPI;
import net.davipatury.fabaobot.apis.twitch.entities.Stream;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.davipatury.fabaobot.player.GuildMusicManager;
import net.davipatury.fabaobot.player.MusicUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class NowPlayingCommand extends Command {

    public NowPlayingCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        GuildMusicManager musicManager = bot.getPlayerController().getGuildAudioPlayer(event.getGuild());
        
        AudioTrack track = musicManager.player.getPlayingTrack();
        if(track != null) {
            EmbedBuilder ebuilder = new EmbedBuilder();
            ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
            ebuilder.setTitle(track.getInfo().title, track.getInfo().uri);
            ebuilder.setAuthor("Tocando agora:", null, null);
            
            Member dj = musicManager.trackDjs.get(track);
            if(dj != null) {
                ebuilder.setFooter("Pedido feito por: " + dj.getEffectiveName(), dj.getUser().getEffectiveAvatarUrl());
            }
            
            if(track.getInfo().isStream) {
                ebuilder.addField("Duração", MusicUtils.formatSeconds(track.getPosition(), "HH:mm:ss") + "/LIVE", true);
            } else {
                ebuilder.addField("Duração", MusicUtils.formatSeconds(track.getPosition()) + "/" + MusicUtils.formatSeconds(track.getDuration()), true);
            }
            ebuilder.addField("Source", track.getSourceManager().getSourceName().toUpperCase(), true);
            
            if(track.getSourceManager() instanceof YoutubeAudioSourceManager) {
                bot.getYoutubeApi().getVideoById(track.getIdentifier(), video -> {
                    ebuilder.setImage(YoutubeAPI.getBetterThumbnail(video.getSnippet().getThumbnails()).getUrl());
                    ebuilder.addField("Likes", NumberFormat.getIntegerInstance().format(video.getStatistics().getLikeCount()) + "\uD83D\uDC4D", true);
                    ebuilder.addField("Dislikes", NumberFormat.getIntegerInstance().format(video.getStatistics().getDislikeCount()) + "\uD83D\uDC4E", true);
                });
            } else if(track.getSourceManager() instanceof TwitchStreamAudioSourceManager) {
                bot.getTwitchApi().searchStreams(track.getIdentifier().replace("https://www.twitch.tv/", ""), 1, streams -> {
                    if(!streams.isEmpty()) {
                        Stream stream = streams.get(0);
                        ebuilder.setThumbnail(stream.getChannel().getLogoUrl());
                        ebuilder.setImage(TwitchAPI.getBetterThumbnailUrl(stream));
                        ebuilder.addField("Assistindo agora", NumberFormat.getIntegerInstance().format(stream.getViewers()) + "\uD83D\uDC41", true);
                        ebuilder.addField("Total de espectadores", NumberFormat.getIntegerInstance().format(stream.getChannel().getViews()) + "\uD83D\uDC40", true);
                    } 
                });
            }
            
            event.getChannel().sendMessage(ebuilder.build()).queue();
        }
    }
	
    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"np"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }
    
    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "Mostra informações sobre a música atual.", false);
        ebuilder.addField("Exemplo", "nowplaying", false);
    }
}
