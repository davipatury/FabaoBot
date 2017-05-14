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
public class ChannelImpl extends Channel {

    private final int id;
    private final String name;
    private final String displayName;
    private final String status;
    private final String broadcasterLanguage;
    private final String language;
    private final String game;
    private final String logoUrl;
    private final String videoBannerUrl;
    private final String profileBannerUrl;
    private final String profileBannerBackgroundColor;
    private final int views;
    private final int followers;
    private final boolean mature;
    private final boolean partner;
    
    public ChannelImpl(JSONObject json) {
        this.id = json.getInt("_id");
        this.name = json.getString("name");
        this.displayName = json.getString("display_name");
        this.status = json.getString("status");
        this.broadcasterLanguage = json.getString("broadcaster_language");
        this.language = json.getString("language");
        this.game = json.getString("game");
        this.logoUrl = json.getString("logo");
        this.videoBannerUrl = json.getString("video_banner");
        this.profileBannerUrl = json.getString("profile_banner");
        this.profileBannerBackgroundColor = json.getString("profile_banner_background_color");
        this.views = json.getInt("views");
        this.followers = json.getInt("followers");
        this.mature = json.getBoolean("mature");
        this.partner = json.getBoolean("partner");
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getBroadcasterLanguage() {
        return broadcasterLanguage;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getGame() {
        return game;
    }

    @Override
    public String getLogoUrl() {
        return logoUrl;
    }

    @Override
    public String getVideoBannerUrl() {
        return videoBannerUrl;
    }

    @Override
    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    @Override
    public String getProfileBannerBackgroundColor() {
        return profileBannerBackgroundColor;
    }

    @Override
    public int getViews() {
        return views;
    }

    @Override
    public int getFollowers() {
        return followers;
    }

    @Override
    public boolean isMature() {
        return mature;
    }

    @Override
    public boolean isPartner() {
        return partner;
    }
    
}
