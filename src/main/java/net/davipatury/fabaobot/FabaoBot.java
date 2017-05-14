/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot;

import net.davipatury.fabaobot.apis.twitch.TwitchAPI;
import net.davipatury.fabaobot.apis.*;
import java.awt.Color;
import net.davipatury.fabaobot.controllers.*;
import net.davipatury.fabaobot.listeners.MessageListener;
import net.davipatury.fabaobot.player.PlayerController;
import net.dv8tion.jda.core.JDA;

/**
 *
 * @author Davi
 */
public class FabaoBot {
    
    public static Color DEFAULT_COLOR = new Color(85, 43, 119);
    
    private final JDA jda;
    private final MemeController memeController;
    private final Configuration configuration;
    private final Statistics statistics;
    private final CommandController commandController;
    private final PlayerController playerController;
    
    private final YoutubeAPI youtubeApi;
    private final TwitchAPI twitchApi;
    
    public FabaoBot(JDA jda, Configuration configuration) {
        this.jda = jda;
        this.jda.addEventListener(new MessageListener(this));
        this.configuration = configuration;
        
        FabaoUtils.createDirectory("data");
        
        this.memeController = new MemeController(this, true);
        this.statistics = new Statistics();
        this.playerController = new PlayerController(this);
        this.commandController = new CommandController(this);
        
        this.youtubeApi = new YoutubeAPI(configuration.getCategory("music").getString("youtube_api_key"));
        this.twitchApi = new TwitchAPI(configuration.getCategory("music").getString("twitch_client_id"));
    }
    
    public void shutdown() {
        shutdown(0);
    }
    
    public void shutdown(int exitCode) {
        jda.shutdown(true);
        System.exit(exitCode);
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Statistics getStatistics() {
        return statistics;
    }
    
    public CommandController getCommandController() {
        return commandController;
    }
    
    public MemeController getMemeController() {
        return memeController;
    }
    
    public PlayerController getPlayerController() {
        return playerController;
    }
    
    public TwitchAPI getTwitchApi() {
        return twitchApi;
    }
    
    public YoutubeAPI getYoutubeApi() {
        return youtubeApi;
    }
}
