package sk.crafting.connectionlogger;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class DatabaseLogging extends Timer {

    static final Object lock = new Object();
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Connection db_connection;
    static HikariDataSource dataSource;
    private boolean connected = false;

    private Timer timer;

    public DatabaseLogging() {
        Init();
        TestConnection();
    }

    private void Init() {
        synchronized (lock) {
            dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(String.format(
                    "jdbc:mysql://%s:%s/%s",
                    ConnectionLogger.getConfigHandler().getDb_host(),
                    ConnectionLogger.getConfigHandler().getDb_port(),
                    ConnectionLogger.getConfigHandler().getDb_name()
            ));
            dataSource.setUsername(ConnectionLogger.getConfigHandler().getDb_user());
            dataSource.setPassword(ConnectionLogger.getConfigHandler().getDb_pass());
            dataSource.setMaximumPoolSize(ConnectionLogger.getConfigHandler().getDb_pools());
            dataSource.setConnectionInitSql(
                    "CREATE TABLE IF NOT EXISTS " + ConnectionLogger.getConfigHandler().getDb_tableName()
                    + "("
                    + "ID int NOT NULL AUTO_INCREMENT, "
                    + "time datetime NOT NULL, "
                    + "type varchar(10) NOT NULL, "
                    + "player_name varchar(50) NOT NULL, "
                    + "player_ip varchar(50) NOT NULL, "
                    + "player_hostname varchar(75) NOT NULL, "
                    + "player_port int(5) NOT NULL, "
                    + "deleted tinyint(1) NOT NULL, "
                    + "PRIMARY KEY (ID)"
                    + ")"
            );
        }
    }

    private void Connect() throws Exception {
        try {
            if (db_connection == null || db_connection.isClosed()) {
                db_connection = dataSource.getConnection();
                ConnectionLogger.getPluginLogger().info("Connected to database");
                connected = true;
                StartTimer();
            }
        } catch (Exception ex) {
            throw ex;
        }

//        String sql = "CREATE TABLE IF NOT EXISTS " + db_tableName
//                + "("
//                + "ID int NOT NULL AUTO_INCREMENT, "
//                + "time datetime NOT NULL, "
//                + "type varchar(10) NOT NULL, "
//                + "player_name varchar(50) NOT NULL, "
//                + "player_ip varchar(50) NOT NULL, "
//                + "player_hostname varchar(75) NOT NULL, "
//                + "player_port int(5) NOT NULL, "
//                + "PRIMARY KEY (ID)"
//                + ")";
    }

    public void Add(EventType type, Calendar time, Player player) {
        PreparedStatement statement = null;
        try {
            Connect();
            synchronized (lock) {
                statement = db_connection.prepareStatement(
                        "INSERT INTO " + ConnectionLogger.getConfigHandler().getDb_tableName() + " (time, type, player_name, player_ip, player_hostname, player_port, deleted) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                statement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time.getTimeInMillis()));
                statement.setString(2, type.getMessage());
                if (player == null) {
                    statement.setString(3, null);
                } else {
                    statement.setString(3, player.getName());
                }
                statement.setString(4, player.getAddress().getAddress().getHostAddress());
                statement.setString(5, player.getAddress().getHostName());
                statement.setInt(6, player.getAddress().getPort());
                statement.setBoolean(7, false);
            }
            statement.executeUpdate();
        } catch (Exception ex) {
            ConnectionLogger.getPluginLogger().severe("Failed to add entry to database: " + ex.toString());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ex) {
                    ConnectionLogger.getPluginLogger().warning("Could not close database statement: " + ex.toString());
                }
            }
        }
    }

    public void AddFromCache(Cache cache) {
        if (cache.getSize() == 0) {
            return;
        }
        PreparedStatement statement = null;
        try {
            Connect();
            for (Log log : cache.toArray()) {
                statement = db_connection.prepareStatement(
                        "INSERT INTO " + ConnectionLogger.getConfigHandler().getDb_tableName() + " (time, type, player_name, player_ip, player_hostname, player_port, deleted) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                statement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getTime().getTimeInMillis()));
                statement.setString(2, log.getType().getMessage());
                if (log.getPlayer() == null) {
                    statement.setString(3, null);
                } else {
                    statement.setString(3, log.getPlayer().getName());
                }
                statement.setString(4, log.getPlayer().getAddress().getAddress().getHostAddress());
                statement.setString(5, log.getPlayer().getAddress().getHostName());
                statement.setInt(6, log.getPlayer().getAddress().getPort());
                statement.setBoolean(7, false);
                statement.executeUpdate();
            }
            cache.Clear();
        } catch (Exception ex) {
            ConnectionLogger.getPluginLogger().severe("Failed to dump cache to database: " + ex.toString());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ex) {
                    ConnectionLogger.getPluginLogger().warning("Could not close database statement: " + ex.toString());
                }
            }
        }
    }

    public void TestConnection() {
        ConnectionLogger.getPluginLogger().info("Testing connection to database...");
        try {
            Connect();
            ConnectionLogger.getPluginLogger().info("Connection to database works!");
        } catch (Exception ex) {
            ConnectionLogger.getPluginLogger().info("Connection to database failed: " + ex.toString());
        }
    }

    public void Clear() {
        PreparedStatement statement = null;
        try {
            Connect();
            statement = db_connection.prepareStatement(
                    "UPDATE " + ConnectionLogger.getConfigHandler().getDb_tableName() + " SET deleted=?"
            );
            statement.setBoolean(1, true);
            statement.executeUpdate();
        } catch (Exception ex) {
            ConnectionLogger.getPluginLogger().severe("Failed to send SQL: " + ex.toString());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ex) {
                    ConnectionLogger.getPluginLogger().warning("Could not close database statement: " + ex.toString());
                }
            }
        }
    }

    private void Disconnect() {
        try {
            if (db_connection != null) {
                db_connection.close();
                connected = false;
                ConnectionLogger.getPluginLogger().info("Connection to database closed");
            }
        } catch (Exception ex) {
            ConnectionLogger.getPluginLogger().warning("Failed to close connection to database: " + ex.toString());
        }
    }

    public void Disable() {
        StopTimer();
        Disconnect();
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public void Reload() {
        StopTimer();
        Disconnect();
        Init();
        TestConnection();
        if(ConnectionLogger.getCache().getSize() > 0) {
            AddFromCache(ConnectionLogger.getCache());
        }
    }

    public ArrayList<String> Get(Calendar max) {
        PreparedStatement statement = null;
        try {
            Connect();
            ResultSet result;
            statement = db_connection.prepareStatement(
                    "SELECT * FROM " + ConnectionLogger.getConfigHandler().getDb_tableName() + " WHERE time>=?"
            );
            statement.setString(1, formatter.format(max.getTimeInMillis()));
            result = statement.executeQuery();
            ArrayList<String> output = new ArrayList<>();
            while (result.next()) {
                if (result.getBoolean("deleted")) {
                    continue;
                }
                String time = result.getString("time");
                output.add(String.format("ID: %s | Time: %s | Type: %s | Player Name: %s | Player IP: %s | Player Hostname: %s | Player Port: %d", result.getString("ID"), time.substring(0, time.lastIndexOf(".")), result.getString("type"), result.getString("player_name"), result.getString("player_ip"), result.getString("player_hostname"), result.getInt("player_port")));
            }
            return output;
        } catch (Exception ex) {
            ConnectionLogger.getPluginLogger().severe("Failed to send SQL: " + ex.toString());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ex) {
                    ConnectionLogger.getPluginLogger().warning("Could not close database statement: " + ex.toString());
                }
            }
        }
        return null;
    }

    private void StartTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Disconnect();
            }
        }, 5 * 60 * 1000);
    }

    private void StopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
