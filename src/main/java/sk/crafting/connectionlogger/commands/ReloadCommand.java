package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class ReloadCommand extends Command
{

    ReloadCommand()
    {
        super("reload", "connectionlogger.cl.reload", true, "Reload ConnectionLogger");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        ConnectionLogger.getInstance().reload();
        sender.sendMessage(ChatColor.GREEN + "ConnectionLogger reloaded");
        return true;
    }

}
