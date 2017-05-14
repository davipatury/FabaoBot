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
public abstract class Stream {
    public abstract String getGame();
    public abstract int getId();
    public abstract int getViewers();
    public abstract int getVideoHeight();
    public abstract int getAverageFps();
    public abstract int getDelay();
    public abstract boolean isPlaylist();
    public abstract JSONObject getPreview();
    public abstract Channel getChannel();
}
