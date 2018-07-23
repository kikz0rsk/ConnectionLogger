package sk.crafting.connectionlogger.cache;

import org.bukkit.ChatColor;
import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.utils.TimeUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Red-Eye~kikz0r_sk
 */
public class Cache {

    private static final String CACHE_FILE_DUMP = "cache_dump.log";

    private final List<Log> cache;
    private final AsyncCacheSender cacheSender = new AsyncCacheSender(this);
    private final SimpleDateFormat formatter = new SimpleDateFormat(TimeUtils.DATABASE_TIME_FORMAT);

    private ConnectionLogger instance = ConnectionLogger.getInstance();

    public Cache(int size) {
        cache = new ArrayList<>(size + 2);
    }

    public Cache(List<? extends Log> collection) {
        cache = new ArrayList<>(collection);
    }

    public synchronized void add(Log log) {
        cache.add(log);

        // If it is full, send immediately
        if (cache.size() >= instance.getConfiguration().getCacheSize()) {
            sendCache(true);
        } else if(!(isEmpty() || isScheduled())) {
            startTimer();
        }
    }

    public synchronized void dumpCacheToFile() {
        File file = new File(ConnectionLogger.getInstance().getDataFolder(), CACHE_FILE_DUMP);
        file.getParentFile().mkdirs();

        StringBuilder builder = new StringBuilder();
        builder.append("-------------------------------------------------------------------------").append(System.lineSeparator());
        builder.append("---------- ConnectionLogger ").append(ConnectionLogger.getInstance().getDescription().getVersion()).append(" CACHE DUMP ").append(formatter.format(Calendar.getInstance().getTimeInMillis())).append(" ----------").append(System.lineSeparator());
        builder.append("-------------------------------------------------------------------------").append(System.lineSeparator());
        for (Log log : getLogs()) {
            builder.append("Time: ").append(formatter.format(log.getTime())).append(System.lineSeparator());
            builder.append("Type: ").append(log.getType()).append(System.lineSeparator());
            builder.append("Player Name: ").append(log.getPlayerName()).append(System.lineSeparator());
            builder.append("Player IP: ").append(log.getPlayerIp()).append(System.lineSeparator());
            builder.append("Player Hostname: ").append(log.getPlayerHostname()).append(System.lineSeparator());
            builder.append("Player Port: ").append(log.getPlayerPort()).append(System.lineSeparator());
            builder.append("World: ").append(log.getWorld()).append(System.lineSeparator());
            builder.append("Session: ").append(log.getSession()).append(System.lineSeparator());
            builder.append("=========================================================================").append(System.lineSeparator());
            builder.append(System.lineSeparator());
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            out.println(builder.toString());
            clear();
            instance.getPluginLogger().log(Level.INFO, "{0}Successfully dumped to file", ChatColor.GREEN);
        } catch (IOException ex) {
            instance.getPluginLogger().log(Level.SEVERE, "IOException while dumping cache to file: {0}", ex.toString());
        }
    }

    public synchronized void sendCache(boolean useFallback) {
        if(instance.getDataSource().send(this)) {
            stopTimer();
            return;
        }
        if(useFallback) {
            dumpCacheToFile();
        }
    }

    public synchronized int getSize() {
        return cache.size();
    }

    public synchronized Log[] toArray() {
        return cache.toArray(new Log[cache.size()]);
    }

    public synchronized boolean isEmpty() {
        return cache.isEmpty();
    }

    public synchronized void clear() {
        cache.clear();
    }

    public synchronized List<Log> getLogs() {
        return cache;
    }

    public void startTimer() {
        cacheSender.startTimer();
    }

    public void stopTimer() {
        cacheSender.stopTimer();
    }

    public boolean isScheduled() {
        return cacheSender.isScheduled();
    }

}
