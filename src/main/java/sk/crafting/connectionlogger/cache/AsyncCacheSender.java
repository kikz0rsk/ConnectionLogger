package sk.crafting.connectionlogger.cache;

import sk.crafting.connectionlogger.ConnectionLogger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Red-Eye~kikz0r_sk
 */
public class AsyncCacheSender {

    private Timer timer;
    private boolean scheduled = false;
    private Cache cache;

    public AsyncCacheSender(Cache cache) {
        this.cache = cache;
    }

    public synchronized void StartTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ConnectionLogger.getInstance().getDatabaseHandler() != null) {
                    ConnectionLogger.getInstance().getPluginLogger().info("Sending cache...");
                    cache.SendCache(false);
                }
                SetScheduled(false);
            }
        }, ConnectionLogger.getInstance().getConfigHandler().getDelayBeforeSend());
        SetScheduled(true);
    }

    private synchronized void SetScheduled(boolean state) {
        scheduled = state;
    }

    public synchronized void StopTimer() {
        if (timer != null) {
            timer.cancel();
            SetScheduled(false);
        }
    }

    public synchronized boolean isScheduled() {
        return scheduled;
    }

}
