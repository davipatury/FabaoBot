/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author Davi
 */
public class FabaoUtils {
    
    // Bytes and files
    public static byte[] fileToBytes(File file) {
        byte[] buffer = null;
        try (InputStream input = new FileInputStream(file)) {
            buffer = new byte[input.available()];
            input.read(buffer);
        } catch (FileNotFoundException ex) {} catch (IOException ex) {}
        return buffer;
    }
    
    public static String bytesToString(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }
    
    public static String fileToString(File file) {
        return bytesToString(fileToBytes(file));
    }
    
    public static File createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            try{
                dir.mkdir();
            } catch(SecurityException se) {}        
        }
        return dir;
    }
    
    // arrayToString
    public static String arrayToString(String[] array) {
	return arrayToString(array, 0, " ");
    }
    
    public static String arrayToString(String[] array, int index) {
	return arrayToString(array, index, " ");
    }
	
    public static String arrayToString(String[] array, int index, String divisor) {
	String word = null;
	int i = index;
	while(i < array.length) {
            if(word == null) {
		word = array[i];
            } else {
		word += divisor + array[i];
            }
            i++;
	}
	return word;
    }
    
    // Log helper
    public static void logToConsole(String text) {
        logToConsole(text, "Bot");
    }
    
    public static void logToConsole(String text, String tag) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	System.out.println("[" + sdf.format(new Date()) + "] [Info] [" + tag + "]: " + text);
    }
    
    // Version formatting
    public static String formatVersion(double version) {
        DecimalFormat df = new DecimalFormat("0.0#");
        return df.format(version).replace(",", ".");
    }
    
    public static String formatVersion(String version) {
        return formatVersion(Double.parseDouble(version));
    }
    
    // Embed helpers
    public static Color getColor(Guild guild) {
        if(guild != null) {
            return guild.getSelfMember().getColor();
        }
        return FabaoBot.DEFAULT_COLOR;
    }
    
    public static String formatUsername(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }
    
    // Misc
    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException exception) {
            return false;
        }
    }
    
    public static void safeDeleteMessage(Message message) {
        try {
            message.delete().queue();
        } catch (PermissionException e) {
            logToConsole("Permission exception caught on message deletion. Missing permission: " + e.getPermission().getName());
        }
    }
}
