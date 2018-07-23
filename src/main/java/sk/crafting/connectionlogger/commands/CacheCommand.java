package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;

import java.util.HashMap;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CacheCommand extends Command
{

    CacheCommand()
    {
        super("cache", "connectionlogger.cl.cache", true, "Show or send cache");

        subCommands = new HashMap<>();
        Command c = new ShowCacheCommand();
        subCommands.put(c.getCommand(), c);
        c = new SendCacheCommand();
        subCommands.put(c.getCommand(), c);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        if(args.length < 2) {
            subCommands.get("show").onCommand(sender, command, string, args);
            return true;
        }

        return route(sender, command, string, args, 1);
    }

}
