package sk.crafting.connectionlogger.cache;

import java.util.Calendar;
import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Log {

    private final Calendar time;
    private final EventType type;
    private final String playerName;
    private final String playerIp;
    private final String playerHostname;
    private final int playerPort;

    public Log(Calendar time, EventType type, String playerName, String playerIp, String playerHostname, int playerPort) {
        this.time = time;
        this.type = type;
        this.playerName = playerName;
        this.playerIp = playerIp;
        this.playerHostname = playerHostname;
        this.playerPort = playerPort;
    }

    public Calendar getTime() {
        return time;
    }

    public EventType getType() {
        return type;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerIp() {
        return playerIp;
    }

    public String getPlayerHostname() {
        return playerHostname;
    }

    public int getPlayerPort() {
        return playerPort;
    }

}
