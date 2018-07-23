package sk.crafting.connectionlogger.commands;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CommandRouter extends Command
{

    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandRouter()
    {
        super("cl", "connectionlogger.cl", true, "Main command with subcommands");
        Command command = new ClearCommand();
        commands.put(command.getName(), command);
        command = new HelpCommand();
        commands.put(command.getName(), command);
        command = new ReloadCommand();
        commands.put(command.getName(), command);
        command = new PrintCommand();
        commands.put(command.getName(), command);
        command = new PrintCacheCommand();
        commands.put(command.getName(), command);
        command = new DumpCacheCommand();
        commands.put(command.getName(), command);
    }

    public HashMap<String, Command> getCommands()
    {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You haven't got permission to do this!");
            return true;
        }
        if (args.length == 0 || args[0].equals("")) {
            return commands.get("help").onCommand(sender, command, string, args);
        }

        Command cmd = commands.get(args[0].toLowerCase());
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
