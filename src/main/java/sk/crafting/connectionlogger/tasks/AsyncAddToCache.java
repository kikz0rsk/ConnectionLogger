package sk.crafting.connectionlogger.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.cache.EventType;
import sk.crafting.connectionlogger.session.SessionManager;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class AsyncAddToCache extends BukkitRunnable
{

    private Log log;

    public AsyncAddToCache(long time, EventType type, Player player)
    {
        log = new Log(
                time, type, player.getName(), player.getAddress().getAddress().getHostAddress(),
                player.getAddress().getAddress().getHostName(), player.getAddress().getPort(), player.getWorld().getName(), SessionManager.getInstance().getSession().getShortHashHex()
        );
    }

    @Override
    public void run()
    {
        ConnectionLogger.getInstance().getCache().add(log);
    }

}
