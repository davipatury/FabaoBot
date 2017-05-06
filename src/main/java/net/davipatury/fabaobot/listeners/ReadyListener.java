/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.listeners;

import net.davipatury.fabaobot.FabaoUtils;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Davi
 */
public class ReadyListener extends ListenerAdapter {
    
    @Override
    public void onReady(ReadyEvent event)
    {
        FabaoUtils.logToConsole("Bot is ready!");
    }
}
