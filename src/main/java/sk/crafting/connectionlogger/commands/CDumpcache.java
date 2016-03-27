package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CDumpcache extends CLCommand
{

    public CDumpcache()
    {
        super( "dumpcache", "connectionlogger.dumpcache", true );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
    {
        if ( ConnectionLogger.getCache().isEmpty() )
        {
            sender.sendMessage( ChatColor.GRAY + "Cache is empty" );
            return true;
        }
        sender.sendMessage( ChatColor.GREEN + "Dumping cache..." );
        ConnectionLogger.getCache().SendCache( true );
        return true;
    }

}
