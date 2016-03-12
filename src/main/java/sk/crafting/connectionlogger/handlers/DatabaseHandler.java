package sk.crafting.connectionlogger.handlers;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Time;
import java.util.logging.Level;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.utils.Utils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class DatabaseHandler
{

    private final SimpleDateFormat formatter = new SimpleDateFormat( Utils.getDatabaseTimeFormat() );

    private Connection db_connection;
    private HikariDataSource dataSource;

    public DatabaseHandler()
    {
        Init();
    }

    private void Init()
    {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl( String.format(
                "jdbc:mysql://%s:%s/%s",
                ConnectionLogger.getConfigHandler().getDb_host(),
                ConnectionLogger.getConfigHandler().getDb_port(),
                ConnectionLogger.getConfigHandler().getDb_name()
        ) );
        dataSource.setUsername( ConnectionLogger.getConfigHandler().getDb_user() );
        dataSource.setPassword( ConnectionLogger.getConfigHandler().getDb_pass() );
        dataSource.setMaximumPoolSize( ConnectionLogger.getConfigHandler().getDb_pools() );
        dataSource.setConnectionInitSql(
                "CREATE TABLE IF NOT EXISTS " + ConnectionLogger.getConfigHandler().getDb_tableName()
                + "("
                + "id int NOT NULL AUTO_INCREMENT, "
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
        dataSource.setConnectionTimeout( ConnectionLogger.getConfigHandler().getTimeout() );
    }

    private void Connect() throws Exception
    {
        if ( db_connection == null || db_connection.isClosed() )
        {
            db_connection = dataSource.getConnection();
            ConnectionLogger.getPluginLogger().info( "Connected to database" );

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
    }

    public boolean AddFromCache( Cache cache )
    {
        if ( cache.isEmpty() )
        {
            return true;
        }
        PreparedStatement statement = null;
        try
        {
            Connect();
            for ( Log log : cache.toArray() )
            {
                statement = db_connection.prepareStatement(
                        "INSERT INTO " + ConnectionLogger.getConfigHandler().getDb_tableName() + " (time, type, player_name, player_ip, player_hostname, player_port, deleted) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                statement.setString( 1, formatter.format( log.getTime() ) );
                statement.setString( 2, log.getType().getMessage() );
                statement.setString( 3, log.getPlayerName() );
                statement.setString( 4, log.getPlayerIp() );
                statement.setString( 5, log.getPlayerHostname() );
                statement.setInt( 6, log.getPlayerPort() );
                statement.setBoolean( 7, false );
                statement.executeUpdate();
            }
            cache.Clear();
            return true;
        } catch ( Exception ex )
        {
            ConnectionLogger.getPluginLogger().log( Level.SEVERE, "Failed to dump cache to database: {0}", ex.toString() );
            return false;
        } finally
        {
            CloseObjects(db_connection, null, statement );
        }
    }

    public void TestConnection()
    {
        ConnectionLogger.getPluginLogger().info( "Testing connection to database..." );
        try
        {
            Connect();
            ConnectionLogger.getPluginLogger().info( "Connection to database works!" );
        } catch ( Exception ex )
        {
            ConnectionLogger.getPluginLogger().info( "Connection to database failed: " + ex.toString() );
        } finally
        {
            Disconnect();
        }
    }

    public void Clear()
    {
        PreparedStatement statement = null;
        try
        {
            Connect();
            statement = db_connection.prepareStatement(
                    "UPDATE " + ConnectionLogger.getConfigHandler().getDb_tableName() + " SET deleted=?"
            );
            statement.setBoolean( 1, true );
            statement.executeUpdate();
        } catch ( Exception ex )
        {
            ConnectionLogger.getPluginLogger().severe( "Failed to send SQL: " + ex.toString() );
        } finally
        {
            CloseObjects(db_connection, null, statement );
        }
    }

    private void Disconnect()
    {
        try
        {
            if ( db_connection != null )
            {
                db_connection.close();
                ConnectionLogger.getPluginLogger().info( "Connection to database closed" );
            }
        } catch ( Exception ex )
        {
            ConnectionLogger.getPluginLogger().warning( "Failed to close connection to database: " + ex.toString() );
        }
    }

    public ArrayList<String> GetLogs( Calendar max )
    {
        PreparedStatement statement = null;
        ResultSet result = null;
        try
        {
            Connect();
            statement = db_connection.prepareStatement(
                    "SELECT * FROM " + ConnectionLogger.getConfigHandler().getDb_tableName() + " WHERE time>=? AND deleted=0"
            );
            statement.setString( 1, formatter.format( max.getTimeInMillis() ) );
            result = statement.executeQuery();
            ArrayList<String> output = new ArrayList<>();
            while ( result.next() )
            {
                Time time = result.getTime( "time" );
                output.add( String.format(
                        "ID: %s | Time: %s | Type: %s | Player Name: %s | Player IP: %s | Player Hostname: %s | Player Port: %d",
                        result.getString( "ID" ),
                        formatter.format( time.getTime() ),
                        result.getString( "type" ),
                        result.getString( "player_name" ),
                        result.getString( "player_ip" ),
                        result.getString( "player_hostname" ),
                        result.getInt( "player_port" )
                ) );
            }
            return output;
        } catch ( Exception ex )
        {
            ConnectionLogger.getPluginLogger().severe( "Failed to send SQL: " + ex.toString() );
        } finally
        {
            CloseObjects(db_connection, result, statement );
        }
        return null;
    }

    private void CloseObjects( Connection connection, ResultSet result, Statement statement )
    {
        try
        {
            if ( result != null )
            {
                result.close();
            }
            if ( statement != null )
            {
                statement.close();
            }
            if(connection != null)
            {
                Disconnect();
            }
        } catch ( SQLException ex )
        {
            ConnectionLogger.getPluginLogger().warning( "Failed to close database statement: " + ex.toString() );
        }
    }

    public void Reload()
    {
        Disconnect();
        Init();
        TestConnection();
        if ( !ConnectionLogger.getCache().isEmpty() )
        {
            ConnectionLogger.getCache().SendCache( false );
        }
    }

    public void Disable()
    {
        Disconnect();
        if ( dataSource != null )
        {
            dataSource.close();
        }
    }

}
