package sk.crafting.connectionlogger.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.cache.Log;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CacheFileDumper {

    private final File file;
    private final SimpleDateFormat formatter = new SimpleDateFormat(DatabaseLogging.getTimeFormat());

    public CacheFileDumper() {
        file = new File(ConnectionLogger.getPlugin().getDataFolder(), "cache_dump.log");
    }

    public void Dump(Cache cache) {
        file.getParentFile().mkdirs();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.println("-------------------------------------------------------------------------");
            out.println("---------- ConnectionLogger " + ConnectionLogger.getPlugin().getDescription().getVersion() + " CACHE DUMP " + formatter.format(Calendar.getInstance().getTimeInMillis()) + " ----------");
            out.println("-------------------------------------------------------------------------");
            for (Log log : cache.getList()) {
                out.println("Time: " + formatter.format(log.getTime().getTimeInMillis()));
                out.println("Type: " + log.getType());
                out.println("Player Name: " + log.getPlayerName());
                out.println("Player IP: " + log.getPlayerIp());
                out.println("Player Hostname: " + log.getPlayerHostname());
                out.println("Player Port: " + log.getPlayerPort());
                out.println("=========================================================================");
                out.println();
            }
            ConnectionLogger.getPluginLogger().log(Level.INFO, "{0}Successfully dumped to file", ChatColor.GREEN);
        } catch (IOException ex) {
            ConnectionLogger.getPluginLogger().log(Level.SEVERE, "IOException while dumping cache to file: {0}", ex.toString());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
