/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.modules;

import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.commands.api.*;
import net.davipatury.fabaobot.controllers.CommandController;

/**
 *
 * @author Davi
 */
public class APIsModule extends Module {

    public APIsModule(CommandController commandController) {
        super(commandController);
    }

    @Override
    public String getName() {
        return "Misc";
    }

    @Override
    public Module generateCommands() {
        commands = new Command[]{
            new WeatherCommand(this),
            new UrbanCommand(this)
        };
        return this;
    }
    
}
