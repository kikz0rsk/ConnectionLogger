package sk.crafting.connectionlogger.commands;

import java.text.SimpleDateFormat;

import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.utils.TimeUtils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class PrintCacheCommand extends Command
{

    public PrintCacheCommand()
    {
        super("printcache", "connectionlogger.cl.printcache", true, "Print current cached logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(TimeUtils.DEFAULT_TIME_FORMAT);
        for (Log log : ConnectionLogger.getInstance().getCache().getList()) {
            sender.sendMessage(String.format("Time: %s | Type: %s | Player Name: %s | Player IP: %s | Player Hostname: %s | Player Port: %d | World: %s",
                    //log.getTime().substring(0, log.getTime().lastIndexOf(".")),
                    formatter.format(log.getTime()),
                    log.getType().getMessage(),
                    log.getPlayerName(),
                    log.getPlayerIp(),
                    log.getPlayerHostname(),
                    log.getPlayerPort(),
                    log.getWorld()
            ));
        }
        return true;
    }

}
