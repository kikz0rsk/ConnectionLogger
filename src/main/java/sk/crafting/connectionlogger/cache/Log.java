package sk.crafting.connectionlogger.cache;

import java.util.Calendar;
import org.bukkit.entity.Player;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Log {
    
    private final Calendar time;
    private final EventType type;
    private final Player player;
    boolean deleted = false;

    public Log(Calendar time, EventType type, Player player) {
        this.time = time;
        this.type = type;
        this.player = player;
    }

    public Calendar getTime() {
        return time;
    }

    public EventType getType() {
        return type;
    }
    
    public Player getPlayer() {
        return player;
    }

    public boolean isDeleted() {
        return deleted;
    }
    
}
