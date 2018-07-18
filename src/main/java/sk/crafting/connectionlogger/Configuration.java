package sk.crafting.connectionlogger;

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
public final class Configuration
{

    private YamlConfiguration conf;
    private final File file;

    private boolean logPlayerConnect,
            logPlayerDisconnect,
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

    public Configuration(ConnectionLogger instance)
    {
        this.instance = instance;
        file = new File(ConnectionLogger.getInstance().getDataFolder(), "config.yml");
        saveDefaultConfig();
        reloadConfig();
    }

    public void saveDefaultConfig()
    {
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                Files.copy(getClass().getResourceAsStream("/config.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                instance.getPluginLogger().log(Level.SEVERE, "Failed to save default config file: {0}", ex.toString());
            }
        }
    }

    public void reloadConfig()
    {
        conf = YamlConfiguration.loadConfiguration(file);
        logPlayerConnect = conf.getBoolean("logging.player-connect", true);
        logPlayerDisconnect = conf.getBoolean("logging.player-disconnect", true);
        cacheSize = conf.getInt("cache.cache-size-trigger", 20);
        if (cacheSize < 2) {
            instance.getPluginLogger().info("Cache size is smaller than required value. Setting to 2");
            cacheSize = 2;
        }
        delayBeforeSend = conf.getInt("cache.delay-before-send", 5000);
        databaseHost = conf.getString("database.host", "localhost");
        databasePort = conf.getString("database.port", "3306");
        databaseUser = conf.getString("database.user");
        databasePassword = conf.getString("database.password");
        databaseName = conf.getString("database.database-name");
        databaseTableName = conf.getString("database.table-name", "connectionlogger");
        databasePools = conf.getInt("database.pool-size", 1);
        timeout = conf.getInt("database.timeout", 5000);
        safeMode = conf.getBoolean("secure-mode", false);
        verbose = conf.getBoolean("verbose", false);
        if (databasePools < 1) {
            instance.getPluginLogger().info("Pool size is smaller than required value. Setting to 1");
            databasePools = 1;
        }
    }

    public void saveConfig()
    {
        try {
            conf.save(file);
        } catch (IOException ex) {
            instance.getPluginLogger().log(Level.SEVERE, "Failed to save configuration file: {0}", ex.toString());
        }
        reloadConfig();
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
