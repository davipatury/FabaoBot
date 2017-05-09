/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.modules;

import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.commands.*;
import net.davipatury.fabaobot.controllers.CommandController;

/**
 *
 * @author Davi
 */
public class AdminModule extends Module {

    public AdminModule(CommandController commandController) {
        super(commandController);
    }

    @Override
    public String getName() {
        return "Admin";
    }

    @Override
    public Module generateCommands() {
        commands = new Command[]{
            new EvalCommand(this),
            new RestartCommand(this),
            new TestCommand(this),
            new UpdateCommand(this)
        };
        return this;
    }
    
}
