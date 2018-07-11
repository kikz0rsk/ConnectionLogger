package sk.crafting.connectionlogger.commands;

import java.util.ArrayList;
import java.util.Calendar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CPrint extends CLCommand
{

    public CPrint()
    {
        super("print", "connectionlogger.cl.print", true, "Print logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
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
        ArrayList<String> result = ConnectionLogger.getInstance().getDatabaseHandler().getLogs(calendar.getTimeInMillis());
        if (result != null) {
            if(result.size() == 0) {
                sender.sendMessage(ChatColor.RED + "No logs in specified time range (" + hours + ")");
                return true;
            }
            sender.sendMessage(result.toArray(new String[result.size()]));
        }
        return true;
    }

}
