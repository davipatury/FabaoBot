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
import java.util.Collection;
import java.util.stream.Collectors;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.commands.Command;
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
                    .accumulate("commands", new JSONObject());
                   
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
        commandController.commandList().forEach(command -> {
            if(!json.getJSONObject("commands").has(command.getName().toLowerCase())) {
                json.getJSONObject("commands").accumulate(command.getName().toLowerCase(), new JSONObject()
                    .put("permissions", new JSONArray(Arrays.asList(command.getPermissions()).stream().map(permission -> permission.toString()).collect(Collectors.toList())))
                    .put("whitelist_channels", new JSONArray())
                    .put("blacklist_channels", new JSONArray())
                );
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
    
    public JSONObject getCommandsPermissions() {
        return json.getJSONObject("commands");
    }
    
    // Conditional functions
    public boolean canUseCommand(MessageReceivedEvent event, Command command) {
        MessageChannel channel = event.getChannel();
        if(!json.getBoolean("permissionIgnoreWhitelist") || !(event.getMember() != null && event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
            if(!whitelistedChannel(channel, command) || blacklistedChannel(channel, command)) {
                return false;
            }
        }
        return hasPermission(event, command);
    }
    
    public boolean whitelistedChannel(MessageChannel channel, Command command) {
        JSONObject cmd = json.getJSONObject("commands").getJSONObject(command.getName().toLowerCase());
        JSONArray whitelistChannels = cmd.getJSONArray("whitelist_channels");
        if(!channel.getType().isGuild() || whitelistChannels.length() < 1) {
            return true;
        }
        return whitelistChannels.toList().contains(channel.getId());
    }
    
    public boolean blacklistedChannel(MessageChannel channel, Command command) {
        JSONObject cmd = json.getJSONObject("commands").getJSONObject(command.getName().toLowerCase());
        JSONArray blacklistChannels = cmd.getJSONArray("blacklist_channels");
        if(!channel.getType().isGuild() || blacklistChannels.length() < 1) {
            return false;
        }
        return blacklistChannels.toList().contains(channel.getId());
    }
    
    public boolean hasPermission(MessageReceivedEvent event, Command command) {
        JSONObject cmd = json.getJSONObject("commands").getJSONObject(command.getName().toLowerCase());
        Collection<Permission> permissions = cmd.getJSONArray("permissions").toList().stream().map(Object::toString).map(Permission::valueOf).collect(Collectors.toList());
        if(permissions.size() < 1) {
            return true;
        } else {
            return event.getMember() != null && event.getMember().hasPermission(permissions);
        }
    }
}
