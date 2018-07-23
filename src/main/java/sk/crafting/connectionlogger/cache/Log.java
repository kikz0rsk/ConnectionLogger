package sk.crafting.connectionlogger.cache;

import org.bukkit.ChatColor;
import sk.crafting.connectionlogger.utils.TimeUtils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Log
{

    private final int id;
    private final long time;
    private final EventType type;
    private final String playerName;
    private final String playerIp;
    private final String playerHostname;
    private final int playerPort;
    private final String world;
    private final String session;

    public Log(long time, EventType type, String playerName, String playerIp, String playerHostname, int playerPort, String world, String session)
    {
        this(-1, time, type, playerName, playerIp, playerHostname, playerPort, world, session);
    }

    public Log(int id, long time, EventType type, String playerName, String playerIp, String playerHostname, int playerPort, String world, String session)
    {
        this.id = id;
        this.time = time;
        this.type = type;
        this.playerName = playerName;
        this.playerIp = playerIp;
        this.playerHostname = playerHostname;
        this.playerPort = playerPort;
        this.world = world;
        this.session = session;
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

    public int getId() {
        return id;
    }

    public String getSession() {
        return session;
    }

    @Override
    public String toString() {
        return String.format(
                "%s%s | %s%s | %s%s | %s%s | %s%s | %s%s | %s%s | %s%s | %s%s",
                ChatColor.LIGHT_PURPLE, id, ChatColor.AQUA, TimeUtils.format(time), ChatColor.BLUE, type, ChatColor.YELLOW, playerName, ChatColor.WHITE,
                playerIp, ChatColor.RED, playerHostname, ChatColor.GREEN, playerPort, ChatColor.GRAY, world, ChatColor.GOLD, session
        );
    }
}
