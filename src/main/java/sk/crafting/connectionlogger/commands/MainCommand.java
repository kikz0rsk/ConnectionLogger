package sk.crafting.connectionlogger.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class MainCommand extends Command
{

    public MainCommand()
    {
        super("cl", "connectionlogger.cl", true, "Main command with subcommands");

        subCommands = new HashMap<>();

        Command command = new ClearCommand();
        subCommands.put(command.getCommand(), command);

        command = new HelpCommand();
        subCommands.put(command.getCommand(), command);
        defaultCommand = command.getCommand();

        command = new ReloadCommand();
        subCommands.put(command.getCommand(), command);

        command = new PrintCommand();
        subCommands.put(command.getCommand(), command);

        command = new CacheCommand();
        subCommands.put(command.getCommand(), command);
    }

    HashMap<String, Command> getCommands()
    {
        return subCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(PERMISSION_DENIED);
            return true;
        }

        if ((args.length == 0 || args[0].equals(""))) {
            Command c = subCommands.get(defaultCommand);
            if(sender.hasPermission(c.getPermission())) {
                return subCommands.get(defaultCommand).onCommand(sender, command, string, args);
            } else {
                sender.sendMessage(PERMISSION_DENIED);
                return true;
            }
        }

        return route(sender, command, string, args, 0);
    }



}
