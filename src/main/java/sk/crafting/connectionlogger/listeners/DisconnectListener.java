package sk.crafting.connectionlogger.listeners;

import java.util.Calendar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.tasks.AsyncAddToCache;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class DisconnectListener implements Listener
{

    @EventHandler( priority = EventPriority.MONITOR )
    public void OnPlayerDisconnect( PlayerQuitEvent event )
    {
        new AsyncAddToCache( EventType.DISCONNECT, System.currentTimeMillis(), event.getPlayer() ).runTaskAsynchronously( ConnectionLogger.getPlugin() );
    }

}
