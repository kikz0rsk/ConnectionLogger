package sk.crafting.connectionlogger.handlers;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.crafting.connectionlogger.Configuration;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.cache.EventType;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.session.SessionManager;
import sk.crafting.connectionlogger.utils.TimeUtils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class DatabaseDataSource implements DataSource
{

    private final SimpleDateFormat formatter = new SimpleDateFormat(TimeUtils.DATABASE_TIME_FORMAT);
    private final SimpleDateFormat defaultFormatter = new SimpleDateFormat(TimeUtils.DEFAULT_TIME_FORMAT);

    private Connection databseConnection;
    private HikariDataSource dataSource;

    private String connectSql;

    private ConnectionLogger plugin;
    private Configuration configuration;
    private Logger logger;

    public DatabaseDataSource(ConnectionLogger plugin)
    {
        this.plugin = plugin;
        configuration = plugin.getConfiguration();
        logger = plugin.getLogger();
        init();
    }

    private void init()
    {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(String.format(
                "jdbc:mysql://%s:%s/%s",
                configuration.getDatabaseHost(),
                configuration.getDatabasePort(),
                configuration.getDatabaseName()
        ));
        dataSource.addDataSourceProperty("useSSL", "false");
        dataSource.setUsername(configuration.getDatabaseUser());
        dataSource.setPassword(configuration.getDatabasePassword());
        dataSource.setMaximumPoolSize(configuration.getDatabasePools());
//        dataSource.setConnectionInitSql(
//                "CREATE TABLE IF NOT EXISTS " + ConnectionLogger.getConfiguration().getDatabaseTableName() + ""
//                + "("
//                + "id int NOT NULL AUTO_INCREMENT, "
//                + "time datetime NOT NULL, "
//                + "type varchar(10) NOT NULL, "
//                + "player_name varchar(50) NOT NULL, "
//                + "player_ip varchar(50) NOT NULL, "
//                + "player_hostname varchar(75) NOT NULL, "
//                + "player_port int(5) NOT NULL, "
//                + "deleted tinyint(1) NOT NULL, "
//                + "PRIMARY KEY (ID)"
//                + ")"
//        );
        dataSource.setConnectionTimeout(configuration.getTimeout());
        connectSql
                = "CREATE TABLE IF NOT EXISTS " + configuration.getDatabaseTableName()
                + "("
                + "id int NOT NULL AUTO_INCREMENT, "
                + "time datetime NOT NULL, "
                + "type varchar(10) NOT NULL, "
                + "player_name varchar(50) NOT NULL, "
                + "player_ip varchar(50) NOT NULL, "
                + "player_hostname varchar(75) NOT NULL, "
                + "player_port int(5) NOT NULL, "
                + "world varchar(50) NOT NULL, "
                + "session varchar(10) NOT NULL, "
                + "deleted tinyint(1) NOT NULL, "
                + "PRIMARY KEY (ID)"
                + ")";
    }

    private void connect() throws Exception
    {
        if (databseConnection == null || databseConnection.isClosed()) {
            databseConnection = dataSource.getConnection();
        }
        PreparedStatement statement = databseConnection.prepareStatement(connectSql);
        statement.executeUpdate();
        closeObjects(null, null, statement);
    }

    @Override
    public boolean send(Cache cache)
    {
        if (cache.isEmpty()) {
            return true;
        }
        PreparedStatement statement = null;
        try {
            connect();
            statement = databseConnection.prepareStatement("INSERT INTO " + configuration.getDatabaseTableName() + " (time, type, player_name, player_ip, player_hostname, player_port, world, session, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (Log log : cache.toArray()) {
                statement.setString(1, formatter.format(log.getTime()));
                statement.setString(2, log.getType().getMessage());
                statement.setString(3, log.getPlayerName());
                statement.setString(4, log.getPlayerIp());
                statement.setString(5, log.getPlayerHostname());
                statement.setInt(6, log.getPlayerPort());
                statement.setString(7, log.getWorld());
                statement.setString(8, SessionManager.getInstance().getSession().getShortHashHex());
                statement.setBoolean(9, false);
                statement.addBatch();
            }
            statement.executeBatch();
            cache.clear();
            return true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to send cache to database: {0}", ex.toString());
            return false;
        } finally {
            closeObjects(databseConnection, null, statement);
        }
    }

    @Override
    public void enable()
    {
        logger.info("Testing connection to database...");
        try {
            connect();
            logger.info("Connection to database works!");
        } catch (Exception ex) {
            logger.log(Level.INFO, "Connection to database failed: {0}", ex.getMessage());
        } finally {
            disconnect();
        }
    }

    @Override
    public void clear()
    {
        PreparedStatement statement = null;
        try {
            connect();
            statement = databseConnection.prepareStatement(
                    "UPDATE " + configuration.getDatabaseTableName() + " SET deleted=?"
            );
            statement.setBoolean(1, true);
            statement.executeUpdate();
        } catch (Exception ex) {
            logger.severe("Failed to send SQL: " + ex.toString());
        } finally {
            closeObjects(databseConnection, null, statement);
        }
    }

    private void disconnect()
    {
        try {
            if (databseConnection != null) {
                databseConnection.close();
            }
        } catch (Exception ex) {
            logger.warning("Failed to close connection to database: " + ex.toString());
        }
    }

    @Override
    public ArrayList<Log> getLogs(long max)
    {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connect();
            statement = databseConnection.prepareStatement(
                    "SELECT * FROM " + configuration.getDatabaseTableName() + " WHERE time>=? AND deleted=0"
            );
            statement.setString(1, formatter.format(max));
            result = statement.executeQuery();
            ArrayList<Log> output = new ArrayList<>();
            while (result.next()) {
                output.add(new Log(
                        Integer.parseInt(result.getString("id")),
                        result.getTimestamp("time").getTime(),
                        EventType.getEventTypeByString(result.getString("type")),
                        result.getString("player_name"),
                        result.getString("player_ip"),
                        result.getString("player_hostname"),
                        result.getInt("player_port"),
                        result.getString("world"),
                        result.getString("session")
                ));
            }
            return output;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to send SQL: {0}", ex.toString());
        } finally {
            closeObjects(databseConnection, result, statement);
        }
        return null;
    }

    private void closeObjects(Connection connection, ResultSet result, Statement statement)
    {
        try {
            if (result != null) {
                result.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                disconnect();
            }
        } catch (SQLException ex) {
            logger.warning("Failed to close database statement: " + ex.toString());
        }
    }

    @Override
    public void reload()
    {
        disable();
        init();
        enable();
        if (!plugin.getCache().isEmpty()) {
            plugin.getCache().sendCache(false);
        }
    }

    @Override
    public void disable()
    {
        disconnect();
        if (dataSource != null) {
            dataSource.close();
        }
    }

}
