/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.commands.misc;

import java.util.Arrays;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class HelpCommand extends Command {

    public HelpCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setColor(FabaoUtils.getColor(event.getGuild()));
        
        String prefix = bot.getConfiguration().getCategory("bot").getString("prefix");
        if(params.length >= 1) {
            String commandName = params[0];
            if(commandName.startsWith(prefix)) {
                commandName = commandName.substring(prefix.length());
            }
            Command command = bot.getCommandController().getCommand(commandName, true);
            
            if(command != null) {
                command.helpEmbed(ebuilder);
                ebuilder.setTitle(command.getName(), null);
                ebuilder.setFooter(command.getModule().getName() + " Module", null);
                if(command.getAliases().length >= 1) {
                    ebuilder.addField("Aliases", String.join(", ", command.getAliases()), false);
                }
            } else {
                ebuilder.setDescription("\u2757 Comando não encontrado!");
            }
        } else {
            ebuilder.setTitle("Command List", null);
            
            bot.getCommandController().moduleList().stream().sorted((mdl, mdl2) -> {
                return mdl.getName().compareTo(mdl2.getName());
            }).forEach(commandModule -> {
                StringBuilder commandList = new StringBuilder();
                Arrays.asList(commandModule.getCommands()).stream().sorted((cmd, cmd2) -> {
                    return cmd.getName().compareTo(cmd2.getName());
                }).forEach(command -> {
                    commandList.append(prefix).append(command.getName()).append("\n");
                });
                ebuilder.addField(commandModule.getName(), commandList.toString(), false);
            });
        }
        
        event.getChannel().sendMessage(ebuilder.build()).queue();
    }
	
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"ajuda"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return true;
    }

    @Override
    public void helpEmbed(EmbedBuilder ebuilder) {
        ebuilder.addField("Descrição", "HELP ME!", false);
        ebuilder.addField("Exemplo", "help, help <comando>", false);
    }
}
