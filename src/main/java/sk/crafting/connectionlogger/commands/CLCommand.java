package sk.crafting.connectionlogger.commands;

import org.bukkit.command.CommandExecutor;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public abstract class CLCommand implements CommandExecutor
{

    protected String name;
    protected String permission;
    protected boolean hasPermission;
    protected String description;

    public CLCommand(String name, String permission, boolean hasPermission, String description)
    {
        this.name = name;
        this.permission = permission;
        this.hasPermission = hasPermission;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public String getPermission()
    {
        return permission;
    }

    public boolean hasPermission()
    {
        return hasPermission;
    }

    public String getDescription()
    {
        return description;
    }

}
