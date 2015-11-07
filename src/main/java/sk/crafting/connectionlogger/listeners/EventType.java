package sk.crafting.connectionlogger.listeners;

import java.util.Calendar;
import sk.crafting.connectionlogger.cache.Log;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public enum EventType {

    CONNECT("Connect"), DISCONNECT("Disconnect"), PLUGIN_SHUTDOWN("Plugin Shutdown");

    private final String msg;

    private EventType(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

    public static Log getPluginShutdownLog() {
        return new Log(Calendar.getInstance(), PLUGIN_SHUTDOWN, "", "", "", 0);
    }
    
}
