/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.apis.twitch.entities;

/**
 *
 * @author Davi
 */
public abstract class Channel {
    public abstract int getId();
    public abstract String getName();
    public abstract String getDisplayName();
    public abstract String getStatus();
    public abstract String getBroadcasterLanguage();
    public abstract String getLanguage();
    public abstract String getGame();
    public abstract String getLogoUrl();
    public abstract String getVideoBannerUrl();
    public abstract String getProfileBannerUrl();
    public abstract String getProfileBannerBackgroundColor();
    public abstract int getViews();
    public abstract int getFollowers();
    public abstract boolean isMature();
    public abstract boolean isPartner();
}
