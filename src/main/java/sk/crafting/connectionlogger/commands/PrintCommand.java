package sk.crafting.connectionlogger.commands;

import java.util.ArrayList;
import java.util.Calendar;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.utils.TimeUtils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class PrintCommand extends Command
{

    PrintCommand()
    {
        super("print", "connectionlogger.cl.print", true, "Print logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        Calendar calendar = Calendar.getInstance();
        int hours = -24;
        if (args.length >= 2) {
            try {
                hours = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                sender.sendMessage(ChatColor.RED + "Failed to parse number!");
                return true;
            }
            if (hours == 0) {
                sender.sendMessage(ChatColor.RED + "Invalid number");
                return true;
            }
            if (hours > 0);
            {
                hours = -hours;
            }
        }
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        ArrayList<Log> result = ConnectionLogger.getInstance().getDataSource().getLogs(calendar.getTimeInMillis());
        if (result != null) {
            if(result.size() == 0) {
                sender.sendMessage(ChatColor.RED + "No logs in specified time range (" + hours + ")");
                return true;
            }
            sender.sendMessage(String.format("%sID | %sTime | %sType | %sPlayer name | %sPlayer IP | %sPlayer hostname | %sPlayer port | %sWorld | %sSession",
                    ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.BLUE, ChatColor.YELLOW, ChatColor.WHITE, ChatColor.RED, ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD));
            sender.sendMessage(format(result).toArray(new String[result.size()]));
        }
        return true;
    }

    private static ArrayList<String> format(ArrayList<Log> logs) {
        ArrayList<String> output = new ArrayList<>(logs.size());
        for (Log log : logs) {
            output.add(String.format(
                    "%s%s | %s%s | %s%s | %s%s | %s%s | %s%s | %s%s | %s%s | %s%s",
                    ChatColor.LIGHT_PURPLE, log.getId(), ChatColor.AQUA, TimeUtils.format(log.getTime()), ChatColor.BLUE, log.getType(), ChatColor.YELLOW, log.getPlayerName(), ChatColor.WHITE,
                    log.getPlayerIp(), ChatColor.RED, log.getPlayerHostname(), ChatColor.GREEN, log.getPlayerPort(), ChatColor.GRAY, log.getWorld(), ChatColor.GOLD, log.getSession()
            ));
        }
        return output;
    }

}
