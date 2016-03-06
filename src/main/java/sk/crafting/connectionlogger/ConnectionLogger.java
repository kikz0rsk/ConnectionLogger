package sk.crafting.connectionlogger;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.listeners.ConnectListener;
import sk.crafting.connectionlogger.listeners.DisconnectListener;
import sk.crafting.connectionlogger.handlers.ConfigurationHandler;
import sk.crafting.connectionlogger.handlers.DatabaseLogging;
import sk.crafting.connectionlogger.utils.Utils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConnectionLogger extends JavaPlugin
{

    private static DatabaseLogging defaultDatabaseHandler;
    private static ConnectionLogger plugin;
    private static Logger logger;
    private static ConfigurationHandler configHandler;
    private static Cache cache;

    @Override
    public void onEnable()
    {
        System.setProperty( org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "warn" );
        getCommand( "cl" ).setExecutor( new Commands() );
        ConnectionLogger.plugin = this;
        logger = plugin.getLogger();
        configHandler = new ConfigurationHandler();
        cache = new Cache( configHandler.getCacheSize() );
        defaultDatabaseHandler = new DatabaseLogging();
        defaultDatabaseHandler.TestConnection();
        logger.log( Level.INFO, "Pool Size: {0}", configHandler.getDb_pools() );
        logger.log( Level.INFO, "Cache Size: {0}", configHandler.getCacheSize() );
        if ( configHandler.isLogPlayerConnect() )
        {
            Bukkit.getPluginManager().registerEvents( new ConnectListener(), this );
        }
        if ( configHandler.isLogPlayerDisconnect() )
        {
            Bukkit.getPluginManager().registerEvents( new DisconnectListener(), this );
        }
    }

    @Override
    public void onDisable()
    {
        Disable();
    }

    public void Reload()
    {
        configHandler.SaveDefaultConfig();
        defaultDatabaseHandler.Reload();
        logger.log( Level.INFO, "Pool Size: {0}", configHandler.getDb_pools() );
        logger.log( Level.INFO, "Cache Size: {0}", configHandler.getCacheSize() );
        if ( !cache.isEmpty() )
        {
            cache = new Cache( cache.getList() );
        }
    }

    private void Disable()
    {
        cache.StopTimer();
        if ( configHandler.isAutoClean() )
        {
            defaultDatabaseHandler.Clear();
        }
        if ( configHandler.isLogPluginShutdown() )
        {
            cache.Add( Utils.getPluginShutdownLog() );
        }
        cache.SendCache( true );
        defaultDatabaseHandler.Disable();
    }

    public static ConnectionLogger getPlugin()
    {
        return plugin;
    }

    public static Logger getPluginLogger()
    {
        return logger;
    }

    public static ConfigurationHandler getConfigHandler()
    {
        return configHandler;
    }

    public static DatabaseLogging getDefaultDatabaseHandler()
    {
        return defaultDatabaseHandler;
    }

    public static Cache getCache()
    {
        return cache;
    }

}
