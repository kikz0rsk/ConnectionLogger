package sk.crafting.connectionlogger.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Cache {
    
    private final List<Log> cache;

    public Cache(int size) {
        cache = Collections.synchronizedList(new ArrayList(size));
    }
    
    public void Add(Log log) {
        synchronized(cache) {
            cache.add(log);
        }
    }
    
    public void Add(Calendar time, EventType type, Player player) {
        Add(new Log(time, type, player));
    }
    
    public int getSize() {
        synchronized(cache) {
            return cache.size();
        }
    }
    
    public Log[] toArray() {
        return cache.toArray(new Log[cache.size()]);
    }
    
}
