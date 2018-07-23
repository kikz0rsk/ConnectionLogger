package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;

public class ShowCacheCommand extends Command {

    public ShowCacheCommand() {
        super("show", "connectionlogger.cl.cache.show", true, "Show cache");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if(ConnectionLogger.getInstance().getCache().isEmpty()) {
            sender.sendMessage(ChatColor.AQUA + "Cache is empty");
            return true;
        }
        for (Log log : ConnectionLogger.getInstance().getCache().getLogs()) {
            sender.sendMessage(log.toString());
        }
        return true;
    }
}
