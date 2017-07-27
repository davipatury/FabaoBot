/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands;

import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public abstract class Command {
    protected Module module;
    public Command(Module module) {
        this.module = module;
    }
    public Module getModule() {
        return module;
    }
    
    public boolean canProcess(final MessageReceivedEvent event, final String[] parameters, final FabaoBot bot) {
        return verifyParameters(parameters);
    }
    
    public abstract void processCommand(final MessageReceivedEvent event, final String[] parameters, final FabaoBot bot);
    public abstract String getName();
    public abstract String[] getAliases();
    public abstract Permission[] getPermissions();
    public abstract boolean verifyParameters(final String[] parameters);
    public abstract void helpEmbed(EmbedBuilder ebuilder);
}
