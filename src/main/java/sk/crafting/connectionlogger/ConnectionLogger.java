package sk.crafting.connectionlogger;

import java.util.Calendar;
import java.util.logging.Level;
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
    private static ConnectionLogger plugin;
    private static Logger logger;
    private static Configuration configHandler;
    private static Cache cache;

    @Override
    public void onEnable() {
        getCommand("cl").setExecutor(new Commands());
        ConnectionLogger.plugin = this;
        logger = plugin.getLogger();
        configHandler = new Configuration();
        cache = new Cache(configHandler.getCacheSize());
        defaultDatabaseHandler = new DatabaseLogging();
        logger.log(Level.INFO, "Pool Size: {0}", configHandler.getDb_pools());
        logger.log(Level.INFO, "Cache Size: {0}", configHandler.getCacheSize());
        if (configHandler.isLogPlayerConnect()) {
            Bukkit.getPluginManager().registerEvents(new ConnectListener(), this);
        }
        if (configHandler.isLogPlayerDisconnect()) {
            Bukkit.getPluginManager().registerEvents(new DisconnectListener(), this);
        }
    }

    public void Reload() {
        ConnectionLogger.getConfigHandler().SaveDefaultConfig();
        ConnectionLogger.getConfigHandler().ReloadConfig();
        ConnectionLogger.getDefaultDatabaseHandler().Reload();
        if (!cache.isEmpty()) {
            logger.warning("Cache is not empty!");
            cache = new Cache(cache.getList());
        }
    }

    @Override
    public void onDisable() {
        if (defaultDatabaseHandler != null) {
            if (configHandler.isAutoClean()) {
                defaultDatabaseHandler.Clear();
            }
            if (!cache.isEmpty()) {
                defaultDatabaseHandler.AddFromCache(cache);
                if (cache.getSize() > 0) {
                    logger.warning("Cache is not empty!");
                }
            }
            if (configHandler.isLogPluginShutdown()) {
                defaultDatabaseHandler.Add(EventType.PLUGIN_SHUTDOWN, Calendar.getInstance(), null);
            }
            defaultDatabaseHandler.Disable();
        }
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
