/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Davi
 */
public class PermissionController {
    
    final private CommandController commandController;
    final private String path = "data/permissions.json";
    private JSONObject json;
    
    public PermissionController(CommandController commandController) {
        this.commandController = commandController;
        
        try {
            load();
        } catch (IOException ex) {
            FabaoUtils.createDirectory("data");
            try (FileWriter writer = new FileWriter(path)) {
                JSONObject newMemes = new JSONObject()
                    .accumulate("permissionIgnoreWhitelist", true)
                    .accumulate("modules", new JSONObject());
                   
                writer.write(newMemes.toString(4));
            } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
            } catch (IOException ex1) {}
                    
            try {
                load();
            } catch (IOException ex1) {}
        }
    }
    
    private void load() throws IOException {
        json = new JSONObject(new JSONTokener(new FileReader(path)));
        
        populateJson();
    }
    
    private void populateJson() {
        commandController.moduleList().forEach(module -> {
            if(getModulesPermissions().has(module.getName().toLowerCase())) {
                Arrays.asList(module.getCommands()).forEach(command -> {
                    if(!getModuleCommands(module).has(command.getName().toLowerCase())) {
                        getModuleCommands(module).accumulate(command.getName().toLowerCase(), new JSONObject()
                            .put("permissions", new JSONArray(Arrays.asList(command.getPermissions()).stream().map(permission -> permission.toString()).collect(Collectors.toList())))
                            .put("whitelist_channels", new JSONArray())
                            .put("blacklist_channels", new JSONArray())
                        );
                    }
                });
            } else {
                getModulesPermissions().accumulate(module.getName().toLowerCase(),  new JSONObject()
                    .put("enabled", true)
                    .put("whitelist_channels", new JSONArray())
                    .put("blacklist_channels", new JSONArray())
                    .put("commands", new JSONObject())
                    .put("options", module.customizeOptions(new JSONObject()))
                );
                
                Arrays.asList(module.getCommands()).forEach(command -> {
                    getModuleCommands(module).accumulate(command.getName().toLowerCase(), new JSONObject()
                        .put("permissions", new JSONArray(Arrays.asList(command.getPermissions()).stream().map(permission -> permission.toString()).collect(Collectors.toList())))
                        .put("whitelist_channels", new JSONArray())
                        .put("blacklist_channels", new JSONArray())
                    );
                });
            }
        });
        
        save();
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json.toString(4));
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        } catch (IOException ex) {}
    }
    
    public JSONObject getModulesPermissions() {
        return json.getJSONObject("modules");
    }
    
    public JSONObject getModuleOptions(Module module) {
        return getModulesPermissions().getJSONObject(module.getName().toLowerCase()).getJSONObject("options");
    }
    
    public JSONObject getModuleCommands(Module module) {
        return getModulesPermissions().getJSONObject(module.getName().toLowerCase()).getJSONObject("commands");
    }
    
    public JSONObject getPermissions(Command command) {
        return getModuleCommands(command.getModule()).getJSONObject(command.getName().toLowerCase());
    }
    
    // Conditional functions
    public boolean canUseCommand(MessageReceivedEvent event, Command command) {
        if(!getModulesPermissions().getJSONObject(command.getModule().getName().toLowerCase()).getBoolean("enabled")) {
            return false;
        }
        MessageChannel channel = event.getChannel();
        if(!json.getBoolean("permissionIgnoreWhitelist") || !(event.getMember() != null && event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
            if(!moduleWhitelistedChannel(channel, command.getModule()) || moduleBlacklistedChannel(channel, command.getModule())) {
                return false;
            }
            if(!commandWhitelistedChannel(channel, command) || commandBlacklistedChannel(channel, command)) {
                return false;
            }
        }
        return hasPermission(event, command);
    }
    
    // Module whitelisting
    public boolean moduleWhitelistedChannel(MessageChannel channel, Module module) {
        JSONArray whitelistChannels = module.getConfiguration().getJSONArray("whitelist_channels");
        if(!channel.getType().isGuild() || whitelistChannels.length() < 1) {
            return true;
        }
        return whitelistChannels.toList().contains(channel.getId());
    }
    
    public boolean moduleBlacklistedChannel(MessageChannel channel, Module module) {
        JSONArray blacklistChannels = module.getConfiguration().getJSONArray("blacklist_channels");
        if(!channel.getType().isGuild() || blacklistChannels.length() < 1) {
            return false;
        }
        return blacklistChannels.toList().contains(channel.getId());
    }
    
    
    // Command whitelisting
    public boolean commandWhitelistedChannel(MessageChannel channel, Command command) {
        JSONArray whitelistChannels = getPermissions(command).getJSONArray("whitelist_channels");
        if(!channel.getType().isGuild() || whitelistChannels.length() < 1) {
            return true;
        }
        return whitelistChannels.toList().contains(channel.getId());
    }
    
    public boolean commandBlacklistedChannel(MessageChannel channel, Command command) {
        JSONArray blacklistChannels = getPermissions(command).getJSONArray("blacklist_channels");
        if(!channel.getType().isGuild() || blacklistChannels.length() < 1) {
            return false;
        }
        return blacklistChannels.toList().contains(channel.getId());
    }
    
    public boolean hasPermission(MessageReceivedEvent event, Command command) {
        List<Permission> permissions = getPermissions(command).getJSONArray("permissions").toList().stream().map(Object::toString).map(Permission::valueOf).collect(Collectors.toList());
        if(permissions.size() < 1) {
            return true;
        } else {
            return event.getMember() != null && event.getMember().hasPermission(permissions);
        }
    }
}
