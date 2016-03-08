package sk.crafting.connectionlogger.tasks;

import java.util.Calendar;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class AsyncAddToCache extends BukkitRunnable
{

    private final EventType type;
    private final long time;
    private final Player player;

    public AsyncAddToCache( EventType type, long time, Player player )
    {
        this.type = type;
        this.time = time;
        this.player = player;
    }

    @Override
    public void run()
    {
        ConnectionLogger.getCache().Add( time, type, player );
        if ( !( ConnectionLogger.getCache().isEmpty() || ConnectionLogger.getCache().isScheduled() ) )
        {
            ConnectionLogger.getCache().StartTimer();
        }
    }

}
