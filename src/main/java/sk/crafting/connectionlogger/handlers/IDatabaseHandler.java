package sk.crafting.connectionlogger.handlers;

import java.util.ArrayList;
import sk.crafting.connectionlogger.cache.Cache;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public interface IDatabaseHandler
{

    public boolean send(Cache cache);

    public void testConnection();

    public void clear();

    public ArrayList<String> getLogs(long max);

    public void reload();

    public void disable();

}
