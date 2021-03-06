package sk.crafting.connectionlogger.commands;

import java.text.SimpleDateFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Log;
import sk.crafting.connectionlogger.utils.Utils;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CPrintcache extends CLCommand
{

    public CPrintcache()
    {
        super("printcache", "connectionlogger.cl.printcache", true, "Print current cached logs");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(Utils.getDefaultTimeFormat());
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
