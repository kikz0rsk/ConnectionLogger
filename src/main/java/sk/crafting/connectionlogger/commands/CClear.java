package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
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
        super("clear", "connectionlogger.clear", true, "Clear all logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
    {
        ConnectionLogger.getInstance().getDatabaseHandler().Clear();
        sender.sendMessage(ChatColor.GREEN + "Logs cleared");
        return true;
    }

}
