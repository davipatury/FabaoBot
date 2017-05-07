/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.commands.*;
import net.davipatury.fabaobot.commands.meme.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class CommandController {
    
    public static class CommandGenerator {
        public static Command[] generateDefaultCommands(FabaoBot bot) {
            return new Command[]{
                new EvalCommand(),
                new TestCommand(),
                new UpdateCommand(),
                new RestartCommand(),
                new MemeCommand(),
                new AddMemeCommand(),
                new RemoveMemeCommand()
            };
        }
    }
    
    private final FabaoBot bot;
    private final List<Command> commandList;
    private final PermissionController permissionController;
    
    public CommandController(FabaoBot bot) {
        this.bot = bot;
        commandList = Arrays.asList(CommandGenerator.generateDefaultCommands(bot));
        
        permissionController = new PermissionController(this);
    }
    
    public CommandController(FabaoBot bot, Command... initialCommands) {
        this.bot = bot;
        commandList = Arrays.asList(initialCommands);
        
        permissionController = new PermissionController(this);
    }
    
    public boolean hasCommand(String commandName, boolean checkForAliases) {
        return commandList.stream().anyMatch(command -> {
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
            return commandList.stream().filter(command -> {
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
        return commandList;
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
