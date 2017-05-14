/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Davi
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingDeque<AudioTrack> queue;
    private final GuildMusicManager manager;

    /**
     * @param player The audio player this scheduler uses
     * @param manager The guild music manager this scheduler is from
     */
    public TrackScheduler(AudioPlayer player, GuildMusicManager manager) {
        this.player = player;
        this.manager = manager;
        this.queue = new LinkedBlockingDeque<>();
    }
    
    public BlockingDeque<AudioTrack> getQueue() {
        return queue;
    }
    
    public AudioPlayer getPlayer() {
        return player;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        queue.addLast(track);
        nextTrack(true);
    }

    public List<AudioTrack> drainQueue() {
        List<AudioTrack> drainedQueue = new ArrayList<>();
        queue.drainTo(drainedQueue);
        return drainedQueue;
    }
    
    public List<AudioTrack> getQueueList() {
        List<AudioTrack> queueList = new ArrayList<>();
        queue.forEach(track -> {
            queueList.add(track);
        });
        return queueList;
    }
    
    /**
     * Start the next track, stopping the current one if it is playing.
     * @param noInterrupt interrupt queue
     */
    public void nextTrack(boolean noInterrupt) {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        AudioTrack next = queue.pollFirst();
        if (next != null) {
            if (!player.startTrack(next, noInterrupt)) {
                queue.addFirst(next);
            }
        } else {
            player.stopTrack();
        }
    }
    
    public void nextTrack() {
        nextTrack(false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
        
        manager.onTrackEnd(track, endReason);
    }
    
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        manager.onTrackStart(track);
    }
}
