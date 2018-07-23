package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ClearCommand extends Command
{

    ClearCommand()
    {
        super("clear", "connectionlogger.cl.clear", true, "Clear all logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        ConnectionLogger.getInstance().getDataSource().clear();
        sender.sendMessage(ChatColor.GREEN + "Logs cleared");
        return true;
    }

}
