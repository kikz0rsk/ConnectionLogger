package sk.crafting.connectionlogger.utils;

import java.util.Calendar;

import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Utils {
    
    public static String getDatabaseTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
    
    public static String getDefaultTimeFormat() {
        //return "yyyy-MM-dd HH:mm:ss";
        return "dd-MM-yyyy HH:mm:ss";
    }
    
    public static Log getPluginShutdownLog() {
        return new Log(Calendar.getInstance(), EventType.PLUGIN_SHUTDOWN, "", "", "", 0);
    }
    
}
