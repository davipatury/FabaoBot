/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.HashMap;
import java.util.Map;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class PlayerController {
    
    private final FabaoBot bot;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    
    public PlayerController(FabaoBot bot) {
        this.bot = bot;
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }
    
    public FabaoBot getBot() {
        return bot;
    }
    
    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager, guild, this);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }
    
    public void loadAndPlay(final TextChannel channel, final String trackUrl, final Member author) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.onTrackLoad(track, author, channel, true);
                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(playlist.isSearchResult()) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();

                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().get(0);
                    }
                    
                    musicManager.onTrackLoad(firstTrack, author, channel, true);
                    play(channel.getGuild(), musicManager, firstTrack);
                } else {
                    playlist.getTracks().forEach(track -> {
                        musicManager.onTrackLoad(track, author, channel, false);
                        play(channel.getGuild(), musicManager, track);
                    });
                
                    musicManager.onPlaylistLoad(playlist, author, channel, true);
                }
            }

            @Override
            public void noMatches() {
                if(!trackUrl.startsWith("ytsearch:") && !trackUrl.startsWith("scsearch:") && !FabaoUtils.isValidURL(trackUrl)) {
                    loadAndPlay(channel, "ytsearch:" + trackUrl, author);
                } else {
                    channel.sendMessage("Nothing found by " + trackUrl).queue();
                }
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }
    
    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());
        
        musicManager.scheduler.queue(track);
    }

    public void skipTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.nextTrack();
    }

    private void connectToFirstVoiceChannel(AudioManager audioManager) {
        if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            Module musicModule = bot.getCommandController().getModule("music");
            VoiceChannel channel = audioManager.getGuild().getVoiceChannelById(bot.getCommandController().getPermissionController().getModuleOptions(musicModule).getJSONObject("voice_channels").getString(audioManager.getGuild().getId()));
            audioManager.openAudioConnection(channel);
        }
    }
    
    public TextChannel getMusicTextChannel(Guild guild) {
        JSONObject moduleOptions = bot.getCommandController().getPermissionController().getModuleOptions(bot.getCommandController().getModule("music"));
        return guild.getTextChannelById(moduleOptions.getJSONObject("text_channels").getString(guild.getId()));
    }
}
