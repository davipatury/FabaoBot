/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot;

import java.awt.Color;
import net.davipatury.fabaobot.controllers.*;
import net.davipatury.fabaobot.listeners.MessageListener;
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
    
    public FabaoBot(JDA jda, Configuration configuration) {
        this.jda = jda;
        this.configuration = configuration;
        commandController = new CommandController(this);
        memeController = new MemeController(this, true);
        statistics = new Statistics();
        
        jda.addEventListener(new MessageListener(this));
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
}
