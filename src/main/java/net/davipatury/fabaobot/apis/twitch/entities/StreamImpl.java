/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.apis.twitch.entities;

import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class StreamImpl extends Stream {
    
    private final String game;
    private final int id;
    private final int viewers;
    private final int videoHeight;
    private final int averageFps;
    private final int delay;
    private final boolean isPlaylist;
    private final JSONObject preview;
    private final Channel channel;
    
    public StreamImpl(JSONObject json) {
        this.id = json.getInt("_id");
        this.game = json.getString("game");
        this.viewers = json.getInt("viewers");
        this.videoHeight = json.getInt("video_height");
        this.averageFps = json.getInt("average_fps");
        this.delay = json.getInt("delay");
        this.isPlaylist = json.getBoolean("is_playlist");
        this.preview = json.getJSONObject("preview");
        this.channel = new ChannelImpl(json.getJSONObject("channel"));
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public String getGame() {
        return game;
    }
    
    @Override
    public int getViewers() {
        return viewers;
    }
    
    @Override
    public int getVideoHeight() {
        return videoHeight;
    }
    
    @Override
    public int getAverageFps() {
        return averageFps;
    }
    
    @Override
    public int getDelay() {
        return delay;
    }
    
    @Override
    public boolean isPlaylist() {
        return isPlaylist;
    }
    
    @Override
    public JSONObject getPreview() {
        return preview;
    }
    
    @Override
    public Channel getChannel() {
        return channel;
    }
}
