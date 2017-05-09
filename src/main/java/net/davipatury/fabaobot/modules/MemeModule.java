/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.modules;

import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.CommandController;
import net.davipatury.fabaobot.commands.meme.*;

/**
 *
 * @author Davi
 */
public class MemeModule extends Module {

    public MemeModule(CommandController commandController) {
        super(commandController);
    }

    @Override
    public String getName() {
        return "Meme";
    }

    @Override
    public Module generateCommands() {
        commands = new Command[]{
            new MemeCommand(this),
            new AddMemeCommand(this),
            new RemoveMemeCommand(this)
        };
        return this;
    }
    
}
