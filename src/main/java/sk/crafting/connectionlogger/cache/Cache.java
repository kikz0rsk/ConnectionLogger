package sk.crafting.connectionlogger.cache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;

import org.bukkit.entity.Player;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.listeners.EventType;
import sk.crafting.connectionlogger.utils.Utils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Cache
{

    private final List<Log> cache;
    private final AsyncCacheSender cacheSender = new AsyncCacheSender( this );
    private final File file = new File( ConnectionLogger.getInstance().getDataFolder(), "cache_dump.log" );
    private final SimpleDateFormat formatter = new SimpleDateFormat( Utils.getDatabaseTimeFormat() );
    
    private ConnectionLogger instance = ConnectionLogger.getInstance();

    public Cache( int size )
    {
        cache = new ArrayList<>( size );
    }

    public Cache( List<? extends Log> collection )
    {
        cache = new ArrayList<>( collection );
    }

    public void Add( Log log )
    {
        synchronized ( this )
        {
            cache.add( log );
        }
        if ( cache.size() >= instance.getConfigHandler().getCacheSize() )
        {
            SendCache( true );
        }
    }

    public void Add( long time, EventType type, Player player )
    {
        Add( new Log( time, type, player.getName(), player.getAddress().getAddress().getHostAddress(), player.getAddress().getAddress().getHostName(), player.getAddress().getPort(), player.getWorld().getName() ) );
        if ( !( isEmpty() || isScheduled() ) )
        {
            StartTimer();
        }
    }

    public synchronized void DumpCacheToFile()
    {
        file.getParentFile().mkdirs();
        PrintWriter out = null;
        try
        {

            StringBuilder builder = new StringBuilder();
            builder.append( "-------------------------------------------------------------------------" ).append( System.lineSeparator() );
            builder.append( "---------- ConnectionLogger " ).append( ConnectionLogger.getInstance().getDescription().getVersion() ).append( " CACHE DUMP " ).append( formatter.format( Calendar.getInstance().getTimeInMillis() ) ).append( " ----------" ).append( System.lineSeparator() );
            builder.append( "-------------------------------------------------------------------------" ).append( System.lineSeparator() );
            for ( Log log : getList() )
            {
                builder.append( "Time: " ).append( formatter.format( log.getTime() ) ).append( System.lineSeparator() );
                builder.append( "Type: " ).append( log.getType() ).append( System.lineSeparator() );
                builder.append( "Player Name: " ).append( log.getPlayerName() ).append( System.lineSeparator() );
                builder.append( "Player IP: " ).append( log.getPlayerIp() ).append( System.lineSeparator() );
                builder.append( "Player Hostname: " ).append( log.getPlayerHostname() ).append( System.lineSeparator() );
                builder.append( "Player Port: " ).append( log.getPlayerPort() ).append( System.lineSeparator() );
                builder.append( "World: " ).append( log.getWorld() ).append( System.lineSeparator() );
                builder.append( "=========================================================================" ).append( System.lineSeparator() );
                builder.append( System.lineSeparator() );
            }
            out = new PrintWriter( new BufferedWriter( new FileWriter( file, true ) ) );
            out.println( builder.toString() );
            Clear();
            instance.getPluginLogger().log( Level.INFO, "{0}Successfully dumped to file", ChatColor.GREEN );
        } catch ( IOException ex )
        {
            instance.getPluginLogger().log( Level.SEVERE, "IOException while dumping cache to file: {0}", ex.toString() );
        } finally
        {
            if ( out != null )
            {
                out.close();
            }
        }
    }

    public synchronized void SendCache( boolean useFallback )
    {
        if ( instance.getDefaultDatabaseHandler().AddFromCache( this ) )
        {
            StopTimer();
        } else if ( useFallback )
        {
            DumpCacheToFile();
        }
    }

    public synchronized int getSize()
    {
        return cache.size();
    }

    public synchronized Log[] toArray()
    {
        return cache.toArray( new Log[ cache.size() ] );
    }

    public synchronized boolean isEmpty()
    {
        return cache.isEmpty();
    }

    public synchronized void Clear()
    {
        cache.clear();
    }

    public synchronized List<Log> getList()
    {
        return cache;
    }

    public void StartTimer()
    {
        cacheSender.StartTimer();
    }

    public void StopTimer()
    {
        cacheSender.StopTimer();
    }

    public boolean isScheduled()
    {
        return cacheSender.isScheduled();
    }

}
