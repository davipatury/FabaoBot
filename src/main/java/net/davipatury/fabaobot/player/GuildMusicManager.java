package net.davipatury.fabaobot.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;
    /**
     * Guild that manager is from.
     */
    public final Guild guild;
    /**
     * Player controller that manager is from.
     */
    public final PlayerController playerController;
    /**
     * Map with members who added an audio track to queue.
     */
    public final Map<AudioTrack, Member> trackDjs;
    
    private final TextChannel musicChannel;
    private final AtomicReference<Message> playingNowMessage;
    private final AtomicBoolean creatingPlayingNowMessage;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     * @param guild Guild that manager is from.
     * @param playerController Player controller that manager is from.
     */
    public GuildMusicManager(AudioPlayerManager manager, Guild guild, PlayerController playerController) {
        this.guild = guild;
        this.playerController = playerController;
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(player, this);
        
        this.trackDjs = new HashMap<>();
        this.playingNowMessage = new AtomicReference<>();
        this.creatingPlayingNowMessage = new AtomicBoolean();
        this.musicChannel = playerController.getMusicTextChannel(guild);
        
        this.player.addListener(scheduler);
    }
    
    /**
     * @return Bot's instance
     */
    public FabaoBot getBot() {
        return playerController.getBot();
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
    
    /**
     * @param track Track which was loaded.
     * @param author Author who queued the track.
     * @param channel Channel where track was queued.
     * @param sendMessage Send info message?
     */
    public void onTrackLoad(AudioTrack track, Member author, TextChannel channel, boolean sendMessage) {
        if(player.getPlayingTrack() != null && sendMessage) {
            channel.sendMessage(MusicMessageHelper.buildTrackMessage(track, author, scheduler, FabaoUtils.getColor(channel.getGuild()), getBot())).queue();
        }
        
        trackDjs.put(track, author);
    }
    
    /**
     * @param playlist Playlist which was loaded.
     * @param author Author who queued the playlist.
     * @param channel Channel where playlist was queued.
     * @param sendMessage Send info message?
     */
    public void onPlaylistLoad(AudioPlaylist playlist, Member author, TextChannel channel, boolean sendMessage) {
        if(sendMessage) {
            channel.sendMessage(MusicMessageHelper.buildPlaylistMessage(playlist, author, FabaoUtils.getColor(channel.getGuild()))).queue();
        }
    }
    
    /**
     * @param track Track which started.
     */
    public void onTrackStart(AudioTrack track) {
        if(!creatingPlayingNowMessage.get()) {
            updatePlayingNowMessage(true);
        }
    }
    
    /**
     * @param track Track which ended.
     * @param endReason Reason why track ended.
     */
    public void onTrackEnd(AudioTrack track, AudioTrackEndReason endReason) {
        trackDjs.remove(track);
        if(!creatingPlayingNowMessage.get()) {
            updatePlayingNowMessage(true);
        }
    }
    
    /**
     * @param newMessage Create a new message?
     */
    public void updatePlayingNowMessage(boolean newMessage) {
        AudioTrack track = player.getPlayingTrack();
        if (track == null || newMessage) {
            Message message = playingNowMessage.getAndSet(null);

            if (message != null) {
                message.delete().complete();
            }
        }

        if (track != null) {
            Message oldPlayingNowMessage = playingNowMessage.get();
            Message newPlayingNowMessage = MusicMessageHelper.buildPlayingNowMessage(track, trackDjs.get(track), FabaoUtils.getColor(guild), getBot());

            if (oldPlayingNowMessage != null) {
                oldPlayingNowMessage.editMessage(newPlayingNowMessage).queue();
            } else {
                if (creatingPlayingNowMessage.compareAndSet(false, true)) {
                    musicChannel.sendMessage(newPlayingNowMessage).queue(created -> {
                        playingNowMessage.set(created);
                        creatingPlayingNowMessage.set(false);
                    }, error -> {
                        creatingPlayingNowMessage.set(false);
                    });
                }
            }
        }
    }
}
