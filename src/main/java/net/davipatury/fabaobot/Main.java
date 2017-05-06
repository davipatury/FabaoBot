/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot;

import net.davipatury.fabaobot.controllers.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.davipatury.fabaobot.listeners.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 *
 * @author Davi
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            Configuration config = new Configuration("config.json", true, true);
            String token = config.getCategory("bot").getString("bot_token");
            if(token != null) {
                JDA jda = new JDABuilder(AccountType.BOT).setToken(token).addEventListener(new ReadyListener()).buildAsync();
                FabaoBot fb = new FabaoBot(jda, config);
            }
        } catch (LoginException | IllegalArgumentException | RateLimitedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
