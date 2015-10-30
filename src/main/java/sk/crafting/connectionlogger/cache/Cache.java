package sk.crafting.connectionlogger.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import sk.crafting.connectionlogger.ConnectionLogger;

import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Cache {

    private final List<Log> cache;

    public Cache(int size) {
        cache = Collections.synchronizedList(new ArrayList<Log>(size));
    }

    public Cache(List<? extends Log> collection) {
        cache = Collections.synchronizedList(new ArrayList<>(collection));
    }

    public void Add(Log log) {
        synchronized (cache) {
            if(cache.size() >= ConnectionLogger.getConfigHandler().getCacheSize()) {
                ConnectionLogger.getDefaultDatabaseHandler().AddFromCache(this);
            }
            cache.add(log);
        }
    }

    public void Add(Calendar time, EventType type, Player player) {
        Add(new Log(time, type, player));
    }

    public int getSize() {
        synchronized (cache) {
            return cache.size();
        }
    }

    public Log[] toArray() {
        synchronized (cache) {
            return cache.toArray(new Log[cache.size()]);
        }
    }

    public boolean isEmpty() {
        synchronized (cache) {
            return cache.isEmpty();
        }
    }

    public void Clear() {
        synchronized (cache) {
            cache.clear();
        }
    }

    public List<Log> getList() {
        synchronized (cache) {
            return cache;
        }
    }

}
