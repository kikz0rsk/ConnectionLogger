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
import sk.crafting.connectionlogger.session.SessionManager;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConnectionLogger extends JavaPlugin
{

    private static ConnectionLogger instance;
    private CommandRouter commandRouter;

    private IDatabaseHandler databaseHandler;
    private SessionManager sessionManager;

    private Logger logger;
    private ConfigurationHandler configHandler;
    private Cache cache;

    @Override
    public void onEnable()
    {
        // Set logging level
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "warn");
           
        commandRouter = new CommandRouter();
        getCommand("cl").setExecutor(commandRouter);
        instance = this;

        logger = getLogger();
        configHandler = new ConfigurationHandler(this);
        if(configHandler.isVerbose()) {
            logger.setLevel(Level.FINEST);
        } else {
            logger.setLevel(Level.INFO);
        }
        cache = new Cache(configHandler.getCacheSize());
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.testConnection();
        sessionManager = SessionManager.getInstance();
        sessionManager.StartSession();
        logger.info("Started new session with ID " + sessionManager.getSession().getHashHex());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable()
    {
        disable();
    }

    public void reload()
    {
        configHandler.saveDefaultConfig();
        configHandler.reloadConfig();
        cache.SendCache(false);
        databaseHandler.reload();
        if (!cache.isEmpty()) {
            cache = new Cache(cache.getList());
            logger.log(Level.INFO, "Cache was not empty during reload - cache size was not changed");
        }
    }

    private void disable()
    {
        cache.StopTimer();
        cache.SendCache(true);
        databaseHandler.disable();
        sessionManager.CloseSession();
    }

    public boolean setCustomDatabaseHandler(IDatabaseHandler handler, Plugin plugin)
    {
        if (handler == null || configHandler.isSafeMode()) {
            return false;
        }
        logger.log(Level.WARNING, "Setting custom database handler. This may cause nonfunctional logging. Name of requesting plugin: {0}", plugin.getName());
        databaseHandler.disable();
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

    public IDatabaseHandler getDatabaseHandler()
    {
        return databaseHandler;
    }

    public Cache getCache()
    {
        return cache;
    }

    public CommandRouter getCommandRouter()
    {
        return commandRouter;
    }

}
