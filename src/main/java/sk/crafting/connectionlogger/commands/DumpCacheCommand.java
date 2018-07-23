package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class DumpCacheCommand extends Command
{

    public DumpCacheCommand()
    {
        super("dumpcache", "connectionlogger.cl.dumpcache", true, "Force to dump cache to database");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        if (ConnectionLogger.getInstance().getCache().isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Cache is empty");
            return true;
        }
        ConnectionLogger.getInstance().getCache().sendCache(true);
        sender.sendMessage(ChatColor.GREEN + "Cache dumped");
        return true;
    }

}
