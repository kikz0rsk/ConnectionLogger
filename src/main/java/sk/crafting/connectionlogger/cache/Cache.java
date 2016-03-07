package sk.crafting.connectionlogger.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.entity.Player;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Cache
{

    private final List<Log> cache;
    private final CacheFileDumper dumper = new CacheFileDumper();
    private final CacheSender cacheSender = new CacheSender( this );

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
        synchronized ( cache )
        {
            if ( cache.size() >= ConnectionLogger.getConfigHandler().getCacheSize() )
            {
                SendCache( true );
            }
            cache.add( log );
        }
    }

    public void Add( Calendar time, EventType type, Player player )
    {
        Add( new Log( time, type, player.getName(), player.getAddress().getAddress().getHostAddress(), player.getAddress().getAddress().getHostName(), player.getAddress().getPort() ) );
    }

    public void DumpCacheToFile()
    {
        synchronized ( cache )
        {
            dumper.Dump( this );
        }
    }

    public void SendCache( boolean useFallback )
    {
        if ( !( ConnectionLogger.getDefaultDatabaseHandler().AddFromCache( this ) ) && useFallback )
        {
            DumpCacheToFile();
            StopTimer();
        }
    }

    public int getSize()
    {
        synchronized ( cache )
        {
            return cache.size();
        }
    }

    public Log[] toArray()
    {
        synchronized ( cache )
        {
            return cache.toArray( new Log[ cache.size() ] );
        }
    }

    public boolean isEmpty()
    {
        synchronized ( cache )
        {
            return cache.isEmpty();
        }
    }

    public void Clear()
    {
        synchronized ( cache )
        {
            cache.clear();
        }
    }

    public List<Log> getList()
    {
        synchronized ( cache )
        {
            return cache;
        }
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
