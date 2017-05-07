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
import net.davipatury.fabaobot.FabaoBot;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Davi
 */
public class MemeController {
    
    private JSONObject json;
    private final FabaoBot bot;
    private final String path;
    
    public MemeController(FabaoBot bot, boolean createIfDoesntExists) {
        this.bot = bot;
        this.path = bot.getConfiguration().getCategory("memes").getString("memes_path");
        
        if(path != null) {
            try {
                load();
            } catch (IOException ex) {
                if(createIfDoesntExists) {
                    try (FileWriter writer = new FileWriter(path)) {
                        JSONObject newMemes = new JSONObject()
                            .accumulate("memes", new JSONObject());
                    
                        writer.write(newMemes.toString(4));
                    } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
                    } catch (IOException ex1) {}
                    
                    try {
                        load();
                    } catch (IOException ex1) {}
                }
            }
        }
    }
    
    private void load() throws IOException {
        JSONTokener jsonTokener = new JSONTokener(new FileReader(path));
        json = new JSONObject(jsonTokener);
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json.toString(4));
        } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
        } catch (IOException ex1) {}
    }
    
    public JSONObject getMemeList() {
        return json.getJSONObject("memes");
    }
    
    public JSONObject getMeme(String name) {
        return json.getJSONObject("memes").optJSONObject(name);
    }
    
    public boolean hasMeme(String name) {
        return json.getJSONObject("memes").has(name);
    }
    
    public void addMeme(String name, JSONObject newMeme) {
        json.getJSONObject("memes").put(name, newMeme);
    }
    
    public void removeMeme(String name) {
        if(json.getJSONObject("memes").has(name)) {
            json.getJSONObject("memes").remove(name);
        }
    }
}
