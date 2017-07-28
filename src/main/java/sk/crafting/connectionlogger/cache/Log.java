package sk.crafting.connectionlogger.cache;

import sk.crafting.connectionlogger.listeners.EventType;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Log
{

    private final long time;
    private final EventType type;
    private final String playerName;
    private final String playerIp;
    private final String playerHostname;
    private final int playerPort;
    private final String world;

    public Log(long time, EventType type, String playerName, String playerIp, String playerHostname, int playerPort, String world)
    {
        this.time = time;
        this.type = type;
        this.playerName = playerName;
        this.playerIp = playerIp;
        this.playerHostname = playerHostname;
        this.playerPort = playerPort;
        this.world = world;
    }

    public long getTime()
    {
        return time;
    }

    public EventType getType()
    {
        return type;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public String getPlayerIp()
    {
        return playerIp;
    }

    public String getPlayerHostname()
    {
        return playerHostname;
    }

    public int getPlayerPort()
    {
        return playerPort;
    }

    public String getWorld()
    {
        return world;
    }

}
