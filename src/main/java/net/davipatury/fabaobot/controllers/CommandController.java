/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.modules.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class CommandController {
    
    public static class DefaultGenerator {
        public static Module[] generateDefaultModules(CommandController commandController) {
            return new Module[]{
                new MemeModule(commandController).generateCommands(),
                new AdminModule(commandController).generateCommands()
            };
        }
    }
    
    private final FabaoBot bot;
    private final List<Module> moduleList;
    private final PermissionController permissionController;
    
    public CommandController(FabaoBot bot) {
        this.bot = bot;
        moduleList = Arrays.asList(DefaultGenerator.generateDefaultModules(this));
        
        permissionController = new PermissionController(this);
    }
    
    public CommandController(FabaoBot bot, Module... initialModules) {
        this.bot = bot;
        moduleList = Arrays.asList(initialModules);
        
        permissionController = new PermissionController(this);
    }
    
    public boolean hasCommand(String commandName, boolean checkForAliases) {
        return commandList().stream().anyMatch(command -> {
            if(command.getName().equalsIgnoreCase(commandName)) {
                return true;
            } else if(checkForAliases && Arrays.asList(command.getAliases()).contains(commandName)) {
                return true;
            }
            return false;
        });
    }
    
    public Command getCommand(String commandName, boolean checkForAliases) {
        try {
            return commandList().stream().filter(command -> {
                if(command.getName().equalsIgnoreCase(commandName)) {
                    return true;
                } else if(checkForAliases && Arrays.asList(command.getAliases()).contains(commandName)) {
                    return true;
                }
                return false;
            }).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    public List<Command> commandList() {
        final List<Command> commands = new ArrayList<>();
        moduleList.stream().map(m -> m.getCommands()).collect(Collectors.toList()).forEach(cmds -> {
            commands.addAll(Arrays.asList(cmds));
        });
        return commands;
    }
    
    public List<Module> moduleList() {
        return moduleList;
    }
    
    public void processCommand(Command command, String[] parameters, MessageReceivedEvent event) {
        if(permissionController.canUseCommand(event, command)) {
            bot.getStatistics().increaseCommandsInSession(1);
            command.processCommand(event, parameters, bot);
        }
    }
    
    public PermissionController getPermissionController() {
        return permissionController;
    }
}
