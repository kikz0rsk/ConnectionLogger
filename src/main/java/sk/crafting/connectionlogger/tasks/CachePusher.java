package sk.crafting.connectionlogger.tasks;

import java.util.Timer;
import java.util.TimerTask;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class CachePusher {
    
    private Timer timer;
    private boolean scheduled = false;
    
    public void StartTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ConnectionLogger.getDefaultDatabaseHandler() != null) {
                    ConnectionLogger.getDefaultDatabaseHandler().AddFromCache(ConnectionLogger.getCache());
                }
            }
        }, 30*1000);
        scheduled = true;
    }
    
    public void StopTimer() {
        if(timer != null) {
            timer.cancel();
            scheduled = false;
        }
    }
    
    public boolean isScheduled() {
        return scheduled;
    }
    
}
