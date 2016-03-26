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

    public CLCommand( String name, String permission, boolean hasPermission )
    {
        this.name = name;
        this.permission = permission;
        this.hasPermission = hasPermission;
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

}
