/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.modules;

import net.davipatury.fabaobot.commands.misc.*;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.CommandController;

/**
 *
 * @author Davi
 */
public class MiscModule extends Module {

    public MiscModule(CommandController commandController) {
        super(commandController);
    }

    @Override
    public String getName() {
        return "Misc";
    }

    @Override
    public Module generateCommands() {
        commands = new Command[]{
            new HelpCommand(this),
            new InfoCommand(this),
            new GuildInfoCommand(this),
            new ProfileCommand(this),
            new PingCommand(this)
        };
        return this;
    }
    
}
