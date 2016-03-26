package sk.crafting.connectionlogger.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.utils.Utils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class MainCommand implements CommandExecutor
{

    private final HashMap<String, CommandExecutor> subCommands = new HashMap<>();

    public MainCommand()
    {
        subCommands.put( "help", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                sender.sendMessage( String.format(
                        "%s%s %sv%s by kikz0r_sk", ChatColor.RED, ConnectionLogger.getPlugin().getDescription().getName(), ChatColor.AQUA, ConnectionLogger.getPlugin().getDescription().getVersion()
                ) );
                return true;
            }
        } );

        subCommands.put( "print", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                Calendar calendar = Calendar.getInstance();
                int hours = -24;
                if ( args.length >= 2 )
                {
                    try
                    {
                        hours = Integer.parseInt( args[ 1 ] );

                    } catch ( NumberFormatException nfe )
                    {
                        sender.sendMessage( ChatColor.RED + "Failed to parse number!" );
                        return true;
                    }
                    if ( hours == 0 )
                    {
                        sender.sendMessage( "Invalid number" );
                        return true;
                    }
                    if ( hours > 0 )
                    {
                        hours = -hours;
                    }
                }
                calendar.add( Calendar.HOUR_OF_DAY, hours );
                ArrayList<String> result = ConnectionLogger.getDefaultDatabaseHandler().GetLogs( calendar.getTimeInMillis() );
                if ( result != null )
                {
                    sender.sendMessage( result.toArray( new String[ result.size() ] ) );
                }
                return true;
            }
        } );

        subCommands.put( "clear", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                sender.sendMessage( "Clearing entries..." );
                ConnectionLogger.getDefaultDatabaseHandler().Clear();
                return true;
            }
        } );

        subCommands.put( "reload", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                sender.sendMessage( ChatColor.GREEN + "Reloading ConnectionLogger..." );
                ConnectionLogger.getPlugin().Reload();
                return true;
            }
        } );

        subCommands.put( "dumpcache", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                if ( ConnectionLogger.getCache().isEmpty() )
                {
                    sender.sendMessage( ChatColor.GRAY + "Cache is empty" );
                    return true;
                }
                sender.sendMessage( ChatColor.GREEN + "Dumping cache..." );
                ConnectionLogger.getCache().SendCache( true );
                return true;
            }
        } );

        subCommands.put( "printcache", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                SimpleDateFormat formatter = new SimpleDateFormat( Utils.getDefaultTimeFormat() );
                for ( Log log : ConnectionLogger.getCache().getList() )
                {
                    sender.sendMessage( String.format( "Time: %s | Type: %s | Player Name: %s | Player IP: %s | Player Hostname: %s | Player Port: %d",
                            //log.getTime().substring(0, log.getTime().lastIndexOf(".")),
                            formatter.format( log.getTime() ),
                            log.getType().getMessage(),
                            log.getPlayerName(),
                            log.getPlayerIp(),
                            log.getPlayerHostname(),
                            log.getPlayerPort()
                    ) );
                }
                return true;
            }
        } );

        subCommands.put( "simulate", new CommandExecutor()
        {
            @Override
            public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
            {
                return true;
            }
        } );

    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
    {
        if ( !sender.hasPermission( "connectionlogger.cl" ) )
        {
            sender.sendMessage( ChatColor.RED + "You haven't got permission!" );
            return true;
        }
        if ( args.length == 0 || args[ 0 ].equals( "" ) )
        {
            return subCommands.get( "help" ).onCommand( sender, command, string, args );
        }
        CommandExecutor cmd = subCommands.get( args[ 0 ].toLowerCase() );
        if ( cmd == null )
        {
            return false;
        }
        return cmd.onCommand( sender, command, string, args );
    }

}
