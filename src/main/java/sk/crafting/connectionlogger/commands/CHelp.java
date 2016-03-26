package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CHelp extends CLCommand
{

    public CHelp()
    {
        super( "help", "connectionlogger.help", true );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String string, String[] args  )
    {
        sender.sendMessage( String.format(
                "%s%s %sv%s by kikz0r_sk", ChatColor.RED, ConnectionLogger.getPlugin().getDescription().getName(), ChatColor.AQUA, ConnectionLogger.getPlugin().getDescription().getVersion()
        ) );
        return true;
    }

}
