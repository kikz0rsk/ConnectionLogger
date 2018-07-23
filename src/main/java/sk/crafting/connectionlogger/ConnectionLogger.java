package sk.crafting.connectionlogger;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.commands.CommandRouter;
import sk.crafting.connectionlogger.handlers.DatabaseDataSource;
import sk.crafting.connectionlogger.handlers.DataSource;
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

    private DataSource dataSource;
    private SessionManager sessionManager;

    private Logger logger;
    private Configuration configuration;
    private Cache cache;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        // Set logging level
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "warn");

        logger = getLogger();

        commandRouter = new CommandRouter();
        getCommand("cl").setExecutor(commandRouter);

        configuration = new Configuration(this);
        cache = new Cache(configuration.getCacheSize());

        dataSource = new DatabaseDataSource(this);
        dataSource.enable();

        sessionManager = SessionManager.getInstance();
        sessionManager.startSession();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable()
    {
        disable();
    }

    public void reload()
    {
        configuration.saveDefaultConfig();
        configuration.reloadConfig();
        cache.sendCache(false);
        dataSource.reload();
        if (!cache.isEmpty()) {
            cache = new Cache(cache.getLogs());
            logger.log(Level.INFO, "Cache was not empty during reload - cache size was not changed");
        }
    }

    private void disable()
    {
        cache.stopTimer();
        cache.sendCache(true);
        dataSource.disable();
        sessionManager.closeSession();
    }

    public boolean setCustomDataSource(DataSource handler, Plugin plugin)
    {
        if (handler == null || configuration.isSafeMode()) {
            return false;
        }
        logger.log(Level.WARNING, "Setting custom database handler. This may cause nonfunctional logging. Name of requesting plugin: {0}", plugin.getName());
        dataSource.disable();
        dataSource = handler;
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

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public DataSource getDataSource()
    {
        return dataSource;
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
