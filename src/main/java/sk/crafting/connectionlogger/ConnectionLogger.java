package sk.crafting.connectionlogger;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.commands.CommandRouter;
import sk.crafting.connectionlogger.handlers.ConfigurationHandler;
import sk.crafting.connectionlogger.handlers.DatabaseHandler;
import sk.crafting.connectionlogger.handlers.IDatabaseHandler;
import sk.crafting.connectionlogger.listeners.PlayerListener;
import sk.crafting.connectionlogger.utils.Utils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConnectionLogger extends JavaPlugin
{

    private static ConnectionLogger instance;

    private IDatabaseHandler databaseHandler;

    private Logger logger;
    private ConfigurationHandler configHandler;
    private Cache cache;

    @Override
    public void onEnable()
    {
        // Set logging level
        System.setProperty( org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "warn" );

        getCommand( "cl" ).setExecutor( new CommandRouter() );
        instance = this;
        logger = getLogger();
        configHandler = new ConfigurationHandler( this );
        cache = new Cache( configHandler.getCacheSize() );
        databaseHandler = new DatabaseHandler( this );
        databaseHandler.TestConnection();
        logger.log( Level.INFO, "Pool Size: {0}", configHandler.getDatabasePools() );
        logger.log( Level.INFO, "Cache Size: {0}", configHandler.getCacheSize() );
        Bukkit.getPluginManager().registerEvents( new PlayerListener(), this );
    }

    @Override
    public void onDisable()
    {
        Disable();
    }

    public void Reload()
    {
        configHandler.SaveDefaultConfig();
        cache.SendCache( false );
        databaseHandler.Reload();
        logger.log( Level.INFO, "Pool Size: {0}", configHandler.getDatabasePools() );
        logger.log( Level.INFO, "Cache Size: {0}", configHandler.getCacheSize() );
        if ( !cache.isEmpty() )
        {
            cache = new Cache( cache.getList() );
            logger.log( Level.INFO, "Cache was not empty during reload - cache size was not changed" );
        }
    }

    private void Disable()
    {
        cache.StopTimer();
        if ( configHandler.isLogPluginShutdown() )
        {
            cache.Add( Utils.getPluginShutdownLog() );
        }
        cache.SendCache( true );
        databaseHandler.Disable();
    }

    public boolean setCustomDatabaseHandler( IDatabaseHandler handler, Plugin plugin )
    {
        if ( handler == null || configHandler.isSafeMode() )
        {
            return false;
        }
        logger.log( Level.WARNING, "Setting custom database handler. This may cause nonfunctional logging. Name of requesting plugin: {0}", plugin.getName() );
        databaseHandler.Disable();
        databaseHandler = handler;
        return true;
    }

    public static ConnectionLogger getInstance()
    {
        return instance;
    }

    public Logger getPluginLogger()
    {
        return logger;
    }

    public ConfigurationHandler getConfigHandler()
    {
        return configHandler;
    }

    public IDatabaseHandler getDefaultDatabaseHandler()
    {
        return databaseHandler;
    }

    public Cache getCache()
    {
        return cache;
    }

}
