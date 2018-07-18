package sk.crafting.connectionlogger.cache;

import sk.crafting.connectionlogger.ConnectionLogger;
import sk.crafting.connectionlogger.utils.Logging;

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

    public synchronized void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ConnectionLogger.getInstance().getDataSource() != null) {
                    cache.sendCache(false);
                    Logging.verbose("Cache sent");
                }
                setScheduled(false);
            }
        }, ConnectionLogger.getInstance().getConfiguration().getDelayBeforeSend());
        setScheduled(true);
    }

    private synchronized void setScheduled(boolean state) {
        scheduled = state;
    }

    public synchronized void stopTimer() {
        if (timer != null) {
            timer.cancel();
            setScheduled(false);
        }
    }

    public synchronized boolean isScheduled() {
        return scheduled;
    }

}
