package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CReload extends CLCommand
{

    public CReload()
    {
        super( "reload", "connectionlogger.reload", true );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
    {
        sender.sendMessage( ChatColor.GREEN + "Reloading ConnectionLogger..." );
        ConnectionLogger.getPlugin().Reload();
        return true;
    }

}
