package sk.crafting.connectionlogger.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.EventType;
import sk.crafting.connectionlogger.tasks.AsyncAddToCache;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class PlayerListener implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerConnect(PlayerJoinEvent event)
    {
        if (ConnectionLogger.getInstance().getConfiguration().isLogPlayerConnect()) {
            new AsyncAddToCache(System.currentTimeMillis(), EventType.CONNECT, event.getPlayer()).runTaskAsynchronously(ConnectionLogger.getInstance());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerDisconnect(PlayerQuitEvent event)
    {
        if (ConnectionLogger.getInstance().getConfiguration().isLogPlayerDisconnect()) {
            new AsyncAddToCache(System.currentTimeMillis(), EventType.DISCONNECT, event.getPlayer()).runTaskAsynchronously(ConnectionLogger.getInstance());
        }
    }

}
