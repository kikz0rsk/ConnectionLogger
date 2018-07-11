package sk.crafting.connectionlogger.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public final class ConfigurationHandler
{

    private YamlConfiguration conf;
    private final File file;

    private boolean logPlayerConnect,
            logPlayerDisconnect,
            logPluginShutdown,
            safeMode, verbose;

    private String databaseHost,
            databasePort,
            databaseUser,
            databasePassword,
            databaseName,
            databaseTableName;
    private int databasePools, timeout;

    private int cacheSize, delayBeforeSend;

    private ConnectionLogger instance;

    public ConfigurationHandler(ConnectionLogger instance)
    {
        this.instance = instance;
        file = new File(ConnectionLogger.getInstance().getDataFolder(), "config.yml");
        SaveDefaultConfig();
    }

    public void SaveDefaultConfig()
    {
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                Files.copy(getClass().getResourceAsStream("/config.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                instance.getPluginLogger().log(Level.SEVERE, "Failed to save default config file: {0}", ex.toString());
            }
        }
        ReloadConfig();
    }

    public void ReloadConfig()
    {
        conf = YamlConfiguration.loadConfiguration(file);
        logPlayerConnect = conf.getBoolean("logging.player-connect");
        logPlayerDisconnect = conf.getBoolean("logging.player-disconnect");
        logPluginShutdown = conf.getBoolean("logging.plugin-shutdown");
        cacheSize = conf.getInt("cache.cache-size-trigger");
        if (cacheSize < 2) {
            instance.getPluginLogger().info("Cache size is smaller than required value. Setting to 2");
            cacheSize = 2;
        }
        delayBeforeSend = conf.getInt("cache.delay-before-send");
        databaseHost = conf.getString("database.host");
        databasePort = conf.getString("database.port");
        databaseUser = conf.getString("database.user");
        databasePassword = conf.getString("database.password");
        databaseName = conf.getString("database.database-name");
        databaseTableName = conf.getString("database.table-name");
        databasePools = conf.getInt("database.pool-size");
        timeout = conf.getInt("database.timeout");
        safeMode = conf.getBoolean("secure-mode");
        verbose = conf.getBoolean("verbose");
        if (databasePools < 1) {
            instance.getPluginLogger().info("Pool size is smaller than required value. Setting to 1");
            databasePools = 1;
        }
    }

    public void SaveConfig()
    {
        try {
            conf.save(file);
        } catch (IOException ex) {
            instance.getPluginLogger().log(Level.SEVERE, "Failed to save configuration file: {0}", ex.toString());
        }
        ReloadConfig();
    }

    public FileConfiguration getConf()
    {
        return conf;
    }

    public boolean isSafeMode()
    {
        return safeMode;
    }

    public boolean isLogPlayerConnect()
    {
        return logPlayerConnect;
    }

    public boolean isLogPlayerDisconnect()
    {
        return logPlayerDisconnect;
    }

    public String getDatabaseHost()
    {
        return databaseHost;
    }

    public String getDatabasePort()
    {
        return databasePort;
    }

    public String getDatabaseUser()
    {
        return databaseUser;
    }

    public String getDatabasePassword()
    {
        return databasePassword;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public String getDatabaseTableName()
    {
        return databaseTableName;
    }

    public int getDatabasePools()
    {
        return databasePools;
    }

    public boolean isLogPluginShutdown()
    {
        return logPluginShutdown;
    }

    public int getCacheSize()
    {
        return cacheSize;
    }

    public int getDelayBeforeSend()
    {
        return delayBeforeSend;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public boolean isVerbose() {
        return verbose;
    }

}
