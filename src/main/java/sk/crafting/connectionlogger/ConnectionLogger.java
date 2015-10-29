package sk.crafting.connectionlogger;

import java.util.Calendar;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.listeners.ConnectListener;
import sk.crafting.connectionlogger.listeners.DisconnectListener;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConnectionLogger extends JavaPlugin {

    private static DatabaseLogging defaultDatabaseHandler;
    static ConnectionLogger plugin;
    static Logger logger;
    static Configuration configHandler;
    static ConnectListener playerListener;
    static Cache cache;

    @Override
    public void onEnable() {
        getCommand("cl").setExecutor(new Commands());
        ConnectionLogger.plugin = this;
        logger = plugin.getLogger();
        configHandler = new Configuration();
        defaultDatabaseHandler = new DatabaseLogging();
        playerListener = new ConnectListener();
        logger.info("Pool Size: " + configHandler.getDb_pools());
        if (configHandler.isLogPlayerConnect()) {
            Bukkit.getPluginManager().registerEvents(new ConnectListener(), this);
        }
        if (configHandler.isLogPlayerDisconnect()) {
            Bukkit.getPluginManager().registerEvents(new DisconnectListener(), this);
        }
    }

    @Override
    public void onDisable() {
        if (configHandler.isAutoClean()) {
            defaultDatabaseHandler.Clear();
        }
        if(configHandler.isLogPluginShutdown()) {
            defaultDatabaseHandler.Add(EventType.PLUGIN_SHUTDOWN.getMessage(), Calendar.getInstance(), null);
        }
        defaultDatabaseHandler.Disable();
    }

    public static ConnectionLogger getPlugin() {
        return plugin;
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    public static Configuration getConfigHandler() {
        return configHandler;
    }

    public static DatabaseLogging getDefaultDatabaseHandler() {
        return defaultDatabaseHandler;
    }

    public static Cache getCache() {
        return cache;
    }

}
