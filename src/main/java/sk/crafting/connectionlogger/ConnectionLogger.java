package sk.crafting.connectionlogger;

import java.util.Calendar;
import sk.crafting.connectionlogger.handlers.ConfigurationHandler;
import sk.crafting.connectionlogger.handlers.DatabaseLogging;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.listeners.ConnectListener;
import sk.crafting.connectionlogger.listeners.DisconnectListener;
import sk.crafting.connectionlogger.listeners.EventType;
import sk.crafting.connectionlogger.tasks.CachePusher;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConnectionLogger extends JavaPlugin {

    private static DatabaseLogging defaultDatabaseHandler;
    private static ConnectionLogger plugin;
    private static Logger logger;
    private static ConfigurationHandler configHandler;
    private static Cache cache;
    private static CachePusher cachePusher;

    @Override
    public void onEnable() {
        getCommand("cl").setExecutor(new Commands());
        ConnectionLogger.plugin = this;
        logger = plugin.getLogger();
        configHandler = new ConfigurationHandler();
        cache = new Cache(configHandler.getCacheSize());
        defaultDatabaseHandler = new DatabaseLogging();
        cachePusher = new CachePusher();
        logger.log(Level.INFO, "Pool Size: {0}", configHandler.getDb_pools());
        logger.log(Level.INFO, "Cache Size: {0}", configHandler.getCacheSize());
        if (configHandler.isLogPlayerConnect()) {
            Bukkit.getPluginManager().registerEvents(new ConnectListener(), this);
        }
        if (configHandler.isLogPlayerDisconnect()) {
            Bukkit.getPluginManager().registerEvents(new DisconnectListener(), this);
        }
    }

    @Override
    public void onDisable() {
        Disable();
    }

    public void Reload() {
        ConnectionLogger.getConfigHandler().SaveDefaultConfig();
        ConnectionLogger.getDefaultDatabaseHandler().Reload();
        logger.log(Level.INFO, "Pool Size: {0}", configHandler.getDb_pools());
        logger.log(Level.INFO, "Cache Size: {0}", configHandler.getCacheSize());
        if (!cache.isEmpty()) {
            logger.warning("Cache is not empty!");
            cache = new Cache(cache.getList());
        }
    }

    public void Disable() {
        cachePusher.StopTimer();
        if (configHandler.isAutoClean()) {
            defaultDatabaseHandler.Clear();
        }
        if (configHandler.isLogPluginShutdown()) {
            cache.Add(EventType.getPluginShutdownLog());
        }
        if (!(defaultDatabaseHandler.AddFromCache(cache))) {
            cache.DumpCacheToFile();
        }
        defaultDatabaseHandler.Disable();
    }

    public static CachePusher getCachePusher() {
        return cachePusher;
    }

    public static ConnectionLogger getPlugin() {
        return plugin;
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    public static ConfigurationHandler getConfigHandler() {
        return configHandler;
    }

    public static DatabaseLogging getDefaultDatabaseHandler() {
        return defaultDatabaseHandler;
    }

    public static Cache getCache() {
        return cache;
    }

}
