package sk.crafting.connectionlogger.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConfigurationHandler {

    private FileConfiguration conf;
    private final File file;

    boolean logPlayerConnect,
            logPlayerDisconnect,
            logPluginShutdown,
            autoClean;

    String db_host,
            db_port,
            db_user,
            db_pass,
            db_name,
            db_tableName;
    int db_pools;

    int cacheSize;

    public ConfigurationHandler() {
        file = new File(ConnectionLogger.getPlugin().getDataFolder(), "config.yml");
        file.mkdirs();
        SaveDefaultConfig();
    }

    public void SaveDefaultConfig() {
        if (!file.exists()) {
            try {
                Files.copy(getClass().getResourceAsStream("/config.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ConnectionLogger.getPluginLogger().severe("Failed to save default config file: " + ex.toString());
            }
        }
        ReloadConfig();
    }

    public void ReloadConfig() {
        conf = YamlConfiguration.loadConfiguration(file);
        logPlayerConnect = conf.getBoolean("logging.player-connect");
        logPlayerDisconnect = conf.getBoolean("logging.player-disconnect");
        logPluginShutdown = conf.getBoolean("logging.plugin-shutdown");
        autoClean = conf.getBoolean("auto-cleaning.server-shutdown");
        cacheSize = conf.getInt("cache.cache-size");
        db_host = conf.getString("database.host");
        db_port = conf.getString("database.port");
        db_user = conf.getString("database.user");
        db_pass = conf.getString("database.password");
        db_name = conf.getString("database.database-name");
        db_tableName = conf.getString("database.table-name");
        db_pools = conf.getInt("database.pools");
    }

    public void SaveConfig() {
        try {
            conf.save(file);
        } catch (IOException ex) {
            ConnectionLogger.getPluginLogger().severe("Failed to save configuration file: " + ex.toString());
        }
        ReloadConfig();
    }

    public FileConfiguration getConf() {
        return conf;
    }

    public boolean isLogPlayerConnect() {
        return logPlayerConnect;
    }

    public boolean isLogPlayerDisconnect() {
        return logPlayerDisconnect;
    }

    public boolean isAutoClean() {
        return autoClean;
    }

    public String getDb_host() {
        return db_host;
    }

    public String getDb_port() {
        return db_port;
    }

    public String getDb_user() {
        return db_user;
    }

    public String getDb_pass() {
        return db_pass;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getDb_tableName() {
        return db_tableName;
    }

    public int getDb_pools() {
        return db_pools;
    }

    public boolean isLogPluginShutdown() {
        return logPluginShutdown;
    }

    public int getCacheSize() {
        return cacheSize;
    }

}
