/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.modules;

import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.CommandController;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public abstract class Module {
    protected Command[] commands;
    final protected CommandController commandController;
    
    public Module(CommandController commandController) {
        this.commandController = commandController;
    }
    
    public abstract String getName();
    public abstract Module generateCommands();
    
    public JSONObject customizeOptions(JSONObject options) {
        return null;
    }
    public Command[] getCommands() {
        return commands;
    };
    public JSONObject getConfiguration() {
        return commandController.getPermissionController().getModulesPermissions().getJSONObject(getName().toLowerCase());
    }
}
