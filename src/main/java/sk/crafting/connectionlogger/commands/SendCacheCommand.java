package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

public class SendCacheCommand extends Command {

    public SendCacheCommand() {
        super("send", "connectionlogger.cl.cache.send", true, "Send cache");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (ConnectionLogger.getInstance().getCache().isEmpty()) {
            sender.sendMessage(ChatColor.AQUA + "Cache is empty");
            return true;
        }
        ConnectionLogger.getInstance().getCache().sendCache(true);
        sender.sendMessage(ChatColor.GREEN + "Cache sent");
        return true;
    }
}
