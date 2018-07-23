package sk.crafting.connectionlogger.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author Red-Eye~kikz0r_sk
 */
public abstract class Command implements CommandExecutor {
    static final String PERMISSION_DENIED = ChatColor.RED + "You haven't got permission to do this!";

    private String command;
    String permission;
    boolean hasPermission;
    private String description;

    HashMap<String, Command> subCommands;
    String defaultCommand;

    Command(String command, String permission, boolean hasPermission, String description) {
        this.command = command;
        this.permission = permission;
        this.hasPermission = hasPermission;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    String getPermission() {
        return permission;
    }

    public boolean hasPermission() {
        return hasPermission;
    }

    String getDescription() {
        return description;
    }

    boolean route(CommandSender sender, org.bukkit.command.Command command, String string, String[] args, int index) {
        Command cmd = subCommands.get(args[index].toLowerCase());
        if (cmd == null) {
            sendUsage(sender);
            return true;
        }

        if (cmd.hasPermission && !sender.hasPermission(cmd.getPermission())) {
            sender.sendMessage(PERMISSION_DENIED);
            return true;
        }
        return cmd.onCommand(sender, command, string, args);
    }

    protected void sendUsage(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        for (Command command : subCommands.values()) {
            sender.sendMessage(String.format("%s%s %s- %s", ChatColor.GOLD, command.getCommand(), ChatColor.LIGHT_PURPLE, command.getDescription()));
        }
    }
}
