package sk.crafting.connectionlogger.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CClear extends CLCommand
{

    public CClear()
    {
        super( "clear", "connectionlogger.clear", true );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
    {
        sender.sendMessage( "Clearing entries..." );
        ConnectionLogger.getInstance().getDefaultDatabaseHandler().Clear();
        return true;
    }

}
