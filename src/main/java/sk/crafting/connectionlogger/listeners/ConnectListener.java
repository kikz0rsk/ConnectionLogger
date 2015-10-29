package sk.crafting.connectionlogger.listeners;

import java.util.Calendar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.tasks.AsyncAddToDatabase;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ConnectListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerConnect(PlayerJoinEvent event) {
        new AsyncAddToDatabase(EventType.CONNECT, Calendar.getInstance(), event.getPlayer()).runTaskAsynchronously(ConnectionLogger.getPlugin());
    }
    
}
