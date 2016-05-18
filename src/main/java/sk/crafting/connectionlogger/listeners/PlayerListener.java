package sk.crafting.connectionlogger.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.tasks.AsyncAddToCache;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class PlayerListener implements Listener
{

    @EventHandler( priority = EventPriority.MONITOR )
    public void OnPlayerConnect( PlayerJoinEvent event )
    {
        if ( ConnectionLogger.getInstance().getConfigHandler().isLogPlayerConnect() )
        {
            new AsyncAddToCache( EventType.CONNECT, System.currentTimeMillis(), event.getPlayer() ).runTaskAsynchronously( ConnectionLogger.getInstance());
        }
    }

    @EventHandler( priority = EventPriority.MONITOR )
    public void OnPlayerDisconnect( PlayerQuitEvent event )
    {
        if ( ConnectionLogger.getInstance().getConfigHandler().isLogPlayerDisconnect() )
        {
            new AsyncAddToCache( EventType.DISCONNECT, System.currentTimeMillis(), event.getPlayer() ).runTaskAsynchronously( ConnectionLogger.getInstance());
        }
    }

}
