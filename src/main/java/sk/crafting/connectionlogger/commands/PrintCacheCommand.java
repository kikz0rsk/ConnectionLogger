package sk.crafting.connectionlogger.commands;

import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class PrintCacheCommand extends Command
{

    PrintCacheCommand()
    {
        super("printcache", "connectionlogger.cl.printcache", true, "Print current cached logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        for (Log log : ConnectionLogger.getInstance().getCache().getLogs()) {
            sender.sendMessage(log.toString());
        }
        return true;
    }

}
