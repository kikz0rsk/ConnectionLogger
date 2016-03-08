package sk.crafting.connectionlogger.cache;

import java.util.Timer;
import java.util.TimerTask;
import sk.crafting.connectionlogger.ConnectionLogger;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class AsyncCacheSender
{

    Timer timer;
    boolean scheduled = false;
    Cache cache;

    private final Object LOCK = new Object();

    public AsyncCacheSender( Cache cache )
    {
        this.cache = cache;
    }

    public void StartTimer()
    {
        timer = new Timer();
        timer.schedule( new TimerTask()
        {
            @Override
            public void run()
            {
                if ( ConnectionLogger.getDefaultDatabaseHandler() != null )
                {
                    ConnectionLogger.getPluginLogger().info( "Sending cache..." );
                    cache.SendCache( false );
                }
                SetScheduled( false );
            }
        }, ConnectionLogger.getConfigHandler().getDelayBeforeSend() );
        SetScheduled( true );
    }

    private void SetScheduled( boolean state )
    {
        synchronized ( LOCK )
        {
            scheduled = state;
        }
    }

    public void StopTimer()
    {
        if ( timer != null )
        {
            timer.cancel();
            SetScheduled( false );
        }
    }

    public boolean isScheduled()
    {
        synchronized ( LOCK )
        {
            return scheduled;
        }
    }

}
