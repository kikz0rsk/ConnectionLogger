package sk.crafting.connectionlogger.utils;

import sk.crafting.connectionlogger.ConnectionLogger;

public class Logging {

    public static void verbose(String str) {
        if(ConnectionLogger.getInstance().getConfiguration().isVerbose()) {
            ConnectionLogger.getInstance().getLogger().info(str);
        }
    }

}
