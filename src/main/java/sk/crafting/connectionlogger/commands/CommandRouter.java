package sk.crafting.connectionlogger.commands;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CommandRouter extends CLCommand
{

    private final HashMap<String, CLCommand> commands = new HashMap<>();

    public CommandRouter()
    {
        super("cl", "connectionlogger.cl", true, "Main command with subcommands");
        CLCommand command = new CClear();
        commands.put(command.getName(), command);
        command = new CHelp();
        commands.put(command.getName(), command);
        command = new CReload();
        commands.put(command.getName(), command);
        command = new CPrint();
        commands.put(command.getName(), command);
        command = new CPrintcache();
        commands.put(command.getName(), command);
        command = new CDumpcache();
        commands.put(command.getName(), command);
    }

    public HashMap<String, CLCommand> getCommands()
    {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
    {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You haven't got permission to do this!");
            return true;
        }
        if (args.length == 0 || args[0].equals("")) {
            return commands.get("help").onCommand(sender, command, string, args);
        }

        CLCommand cmd = commands.get(args[0].toLowerCase());
        if (cmd == null) {
            return false;
        }

        if (cmd.hasPermission && !sender.hasPermission(cmd.getPermission())) {
            sender.sendMessage(ChatColor.RED + "You haven't got permission to do this!");
            return true;
        }
        return cmd.onCommand(sender, command, string, args);
    }

}
