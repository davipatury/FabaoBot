/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
    
    public static String formatVersion(double version) {
        DecimalFormat df = new DecimalFormat("0.0#");
        return df.format(version).replace(",", ".");
    }
    
    public static String formatVersion(String version) {
        return formatVersion(Double.parseDouble(version));
    }
}
