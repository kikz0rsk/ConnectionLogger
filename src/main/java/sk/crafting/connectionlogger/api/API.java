package sk.crafting.connectionlogger.api;

import org.bukkit.plugin.Plugin;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.handlers.IDatabaseHandler;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class API
{

    private static API instance;
    private ConnectionLogger plugin;

    private API(ConnectionLogger plugin)
    {
        this.plugin = plugin;
    }

    public static API getInstance()
    {
        if ( instance == null )
        {
            instance = new API(ConnectionLogger.getInstance());
        }
        return instance;
    }

    public boolean setCustomDatabaseHandler( IDatabaseHandler handler, Plugin caller )
    {
        return this.plugin.setCustomDatabaseHandler( handler, caller );
    }

}
