package sk.crafting.connectionlogger.handlers;

import java.util.ArrayList;
import sk.crafting.connectionlogger.cache.Cache;
import sk.crafting.connectionlogger.cache.Log;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public interface DataSource
{

    public boolean send(Cache cache);

    public void clear();

    public ArrayList<Log> getLogs(long max);

    public void enable();

    public void reload();

    public void disable();

}
