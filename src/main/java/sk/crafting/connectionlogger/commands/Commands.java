//package sk.crafting.connectionlogger.commands;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import sk.crafting.connectionlogger.ConnectionLogger;
//import sk.crafting.connectionlogger.cache.Log;
//import sk.crafting.connectionlogger.utils.Utils;
//
///**
// *
// * @author Red-Eye~kikz0r_sk
// * @deprecated Replaced with new command handling system in MainCommand class
// */
//@Deprecated
//public class Commands implements CommandExecutor
//{
//
//    @Override
//    public boolean onCommand( CommandSender sender, Command command, String string, String[] args )
//    {
//        if ( args.length == 0 || args[ 0 ].equalsIgnoreCase( "help" ) )
//        {
//            sender.sendMessage( String.format(
//                    "%s%s %sv%s by kikz0r_sk", ChatColor.RED, ConnectionLogger.getInstance().getDescription().getName(), ChatColor.AQUA, ConnectionLogger.getInstance().getDescription().getVersion()
//            ) );
//            return true;
//        }
//        if ( args[ 0 ].equalsIgnoreCase( "print" ) )
//        {
//            Calendar calendar = Calendar.getInstance();
//            int hours = -24;
//            if ( args.length >= 2 )
//            {
//                try
//                {
//                    hours = Integer.parseInt( args[ 1 ] );
//                    if ( hours == 0 )
//                    {
//                        sender.sendMessage( "Invalid number" );
//                        return true;
//                    }
//                    if ( hours > 0 )
//                    {
//                        hours = -hours;
//                    }
//                } catch ( NumberFormatException nfe )
//                {
//                    sender.sendMessage( ChatColor.RED + "Failed to parse number!" );
//                    return true;
//                }
//            }
//            calendar.add( Calendar.HOUR_OF_DAY, hours );
//            ArrayList<String> result = ConnectionLogger.getDefaultDatabaseHandler().getLogs( calendar.getTimeInMillis() );
//            sender.sendMessage( result.toArray( new String[ result.size() ] ) );
//            return true;
//        }
//        if ( args[ 0 ].equalsIgnoreCase( "clear" ) )
//        {
//            sender.sendMessage( "Clearing entries..." );
//            ConnectionLogger.getDefaultDatabaseHandler().Clear();
//            return true;
//        }
//        if ( args[ 0 ].equalsIgnoreCase( "reload" ) )
//        {
//            sender.sendMessage( ChatColor.GREEN + "Reloading ConnectionLogger..." );
//            ConnectionLogger.getInstance().Reload();
//            return true;
//        }
//        if ( args[ 0 ].equalsIgnoreCase( "dumpcache" ) )
//        {
//            if ( ConnectionLogger.getCache().isEmpty() )
//            {
//                sender.sendMessage( ChatColor.GRAY + "Cache is empty" );
//                return true;
//            }
//            sender.sendMessage( ChatColor.GREEN + "Dumping cache..." );
//            ConnectionLogger.getCache().SendCache( true );
//            return true;
//        }
//        if ( args[ 0 ].equalsIgnoreCase( "printcache" ) )
//        {
//            SimpleDateFormat formatter = new SimpleDateFormat( Utils.getDefaultTimeFormat() );
//            for ( Log log : ConnectionLogger.getCache().getList() )
//            {
//                sender.sendMessage( String.format( "Time: %s | Type: %s | Player Name: %s | Player IP: %s | Player Hostname: %s | Player Port: %d | World: %s",
//                        //log.getTime().substring(0, log.getTime().lastIndexOf(".")),
//                        formatter.format( log.getTime() ),
//                        log.getType().getMessage(),
//                        log.getPlayerName(),
//                        log.getPlayerIp(),
//                        log.getPlayerHostname(),
//                        log.getPlayerPort(),
//                        log.getWorld()
//                ) );
//            }
//        }
//        return false;
//    }
//
//}
