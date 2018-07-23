package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class HelpCommand extends Command
{

    HelpCommand()
    {
        super("help", "connectionlogger.cl.help", true, "Show commands");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        sender.sendMessage(String.format(
                "%s%s %sv%s by kikz0r_sk", ChatColor.RED, ConnectionLogger.getInstance().getDescription().getName(), ChatColor.AQUA, ConnectionLogger.getInstance().getDescription().getVersion()
        ));
        for(Command cmd : ConnectionLogger.getInstance().getMainCommand().getCommands().values()) {
            sender.sendMessage(String.format("%s%s %s- %s", ChatColor.GOLD, cmd.getCommand(), ChatColor.LIGHT_PURPLE, cmd.getDescription()));
        }
        return true;
    }

}
