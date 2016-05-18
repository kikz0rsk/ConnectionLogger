package sk.crafting.connectionlogger.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.listeners.EventType;
import sk.crafting.connectionlogger.tasks.AsyncAddToCache;
import sk.crafting.connectionlogger.utils.TestPlayer;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CSimulate extends CLCommand
{

    public CSimulate()
    {
        super( "simulate", "connectionlogger.simulate", true );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
    {
        if ( args.length > 1 )
        {
            if ( args[ 1 ].equalsIgnoreCase( "connect" ) )
            {
                new AsyncAddToCache( EventType.CONNECT, System.currentTimeMillis(), new TestPlayer() ).runTaskAsynchronously( ConnectionLogger.getInstance());
            } else
            {
                new AsyncAddToCache( EventType.DISCONNECT, System.currentTimeMillis(), new TestPlayer() ).runTaskAsynchronously( ConnectionLogger.getInstance());
            }
            return true;
        }
        return false;
    }

}
