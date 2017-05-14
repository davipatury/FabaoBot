/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Davi
 */
public class MusicUtils {
    
    public static String formatSeconds(long ms) {
        return formatSeconds(ms, "mm:ss");
    }
        
    public static String formatSeconds(long ms, String format) {
        return new SimpleDateFormat(format).format(msToCalendar(ms).getTime());
    }
    
    public static Calendar msToCalendar(long ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar;
    }
}
