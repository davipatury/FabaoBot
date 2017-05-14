/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.player;

import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.apis.YoutubeAPI;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

/**
 *
 * @author Davi
 */
public class MusicMessageHelper {
    
    public static Message buildPlayingNowMessage(AudioTrack track, Member dj, Color embedColor, FabaoBot bot) {
        MessageBuilder mbuilder = new MessageBuilder();
        final EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(embedColor);
        
        ebuilder.setTitle(track.getInfo().title, track.getInfo().uri);
        ebuilder.setAuthor("Tocando agora:", null, null);
        ebuilder.setFooter("Pedido feito por: " + dj.getEffectiveName(), dj.getUser().getEffectiveAvatarUrl());
        if(track.getInfo().isStream) {
            ebuilder.addField("Duração", "LIVE \uD83D\uDD5B", true);
        } else {
            ebuilder.addField("Duração", MusicUtils.formatSeconds(track.getDuration()) + " \uD83D\uDD5B", true);
        }
        
        if(track.getSourceManager() instanceof YoutubeAudioSourceManager) {
            bot.getYoutubeApi().getVideoById(track.getIdentifier(), video -> {
                ebuilder.setThumbnail(YoutubeAPI.getBetterThumbnail(video.getSnippet().getThumbnails()).getUrl());
            });
        } else if(track.getSourceManager() instanceof TwitchStreamAudioSourceManager) {
            bot.getTwitchApi().searchStreams(track.getIdentifier().replace("https://www.twitch.tv/", ""), 1, streams -> {
                if(!streams.isEmpty()) {
                    ebuilder.setThumbnail(streams.get(0).getChannel().getLogoUrl());
                } 
            });
        }
        
        mbuilder.setEmbed(ebuilder.build());
        return mbuilder.build();
    }
    
    public static Message buildTrackMessage(AudioTrack track, Member dj, TrackScheduler scheduler, Color embedColor, FabaoBot bot) {
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(embedColor);
        
        ebuilder.setTitle(track.getInfo().title, track.getInfo().uri);
        ebuilder.setAuthor("Foi adicionada à lista de reprodução:", null, null);
        ebuilder.setFooter("Pedido feito por: " + dj.getEffectiveName(), dj.getUser().getEffectiveAvatarUrl());
        
        if(track.getInfo().isStream) {
            ebuilder.addField("Duração", "LIVE \uD83D\uDD5B", true);
        } else {
            ebuilder.addField("Duração", MusicUtils.formatSeconds(track.getDuration()) + " \uD83D\uDD5B", true);
        }
        
        long queueSize = scheduler.getQueue().size();
        if(scheduler.getPlayer().getPlayingTrack() != null) {
            queueSize++;
        }
        if(queueSize > 0) {
            ebuilder.addField("Posição", String.valueOf(queueSize), true);
        }
        
        if(track.getSourceManager() instanceof YoutubeAudioSourceManager) {
            bot.getYoutubeApi().getVideoById(track.getIdentifier(), video -> {
                ebuilder.setThumbnail(YoutubeAPI.getBetterThumbnail(video.getSnippet().getThumbnails()).getUrl());
            });
        } else if(track.getSourceManager() instanceof TwitchStreamAudioSourceManager) {
            bot.getTwitchApi().searchStreams(track.getIdentifier().replace("https://www.twitch.tv/", ""), 1, streams -> {
                if(!streams.isEmpty()) {
                    ebuilder.setThumbnail(streams.get(0).getChannel().getLogoUrl());
                } 
            });
        }
        
        mbuilder.setEmbed(ebuilder.build());
        return mbuilder.build();
    }
    
    public static Message buildPlaylistMessage(AudioPlaylist playlist, Member author, Color embedColor) {
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(embedColor);
        
        ebuilder.setTitle(playlist.getName(), null);
        ebuilder.setAuthor("Foi adicionada à lista de reprodução:", null, null);
        ebuilder.setFooter("Pedido feito por: " + author.getEffectiveName(), author.getUser().getEffectiveAvatarUrl());
        ebuilder.addField("Duração", MusicUtils.formatSeconds(playlist.getTracks().stream().filter(t -> !t.getInfo().isStream).mapToLong(t -> t.getDuration()).sum(), "HH:mm:ss") + " \uD83D\uDD5B", true);
        ebuilder.addField("Músicas", playlist.getTracks().size() + " \uD83C\uDFB5", true);
        mbuilder.setEmbed(ebuilder.build());
        return mbuilder.build();
    }
}
