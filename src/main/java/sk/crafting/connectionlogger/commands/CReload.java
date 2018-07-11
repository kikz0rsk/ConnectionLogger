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
        super("reload", "connectionlogger.cl.reload", true, "Reload ConnectionLogger");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
    {
        ConnectionLogger.getInstance().Reload();
        sender.sendMessage(ChatColor.GREEN + "ConnectionLogger reloaded");
        return true;
    }

}
