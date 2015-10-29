package sk.crafting.connectionlogger;

import java.util.ArrayList;
import java.util.Calendar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(String.format(
                    "%s%s %sv%s by kikz0r_sk", ChatColor.RED, ConnectionLogger.getPlugin().getDescription().getName(), ChatColor.AQUA, ConnectionLogger.getPlugin().getDescription().getVersion()
            ));
            return true;
        }
        if (args[0].equalsIgnoreCase("print")) {
            Calendar calendar = Calendar.getInstance();
            int hours = -24;
            if (args.length >= 2) {
                try {
                    hours = Integer.parseInt(args[1]);
                    if (hours > 0) {
                        hours = -hours;
                    }
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(ChatColor.RED + "Failed to parse number!");
                    return true;
                }
            }
            ConnectionLogger.getPluginLogger().info(String.valueOf(hours));
            calendar.add(Calendar.HOUR_OF_DAY, hours);
            ArrayList<String> result = ConnectionLogger.getDefaultDatabaseHandler().Get(calendar);
            sender.sendMessage(result.toArray(new String[result.size()]));
            return true;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            sender.sendMessage("Clearing entries...");
            ConnectionLogger.getDefaultDatabaseHandler().Clear();
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            ConnectionLogger.getConfigHandler().SaveDefaultConfig();
            ConnectionLogger.getConfigHandler().ReloadConfig();
            ConnectionLogger.getDefaultDatabaseHandler().Reload();
            return true;
        }
        if(args[0].equalsIgnoreCase("dumpcache")) {
            if(ConnectionLogger.getCache().getSize() == 0) {
                sender.sendMessage(ChatColor.GRAY + "Cache is empty");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Dumping cache...");
            ConnectionLogger.getDefaultDatabaseHandler().AddFromCache(ConnectionLogger.getCache());
        }
        return false;
    }

}
