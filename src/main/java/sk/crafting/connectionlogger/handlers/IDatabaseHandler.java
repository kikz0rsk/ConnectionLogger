package sk.crafting.connectionlogger.handlers;

import java.util.ArrayList;
import sk.crafting.connectionlogger.cache.Cache;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public interface IDatabaseHandler {
    public boolean AddFromCache(Cache cache);
    public void TestConnection();
    public void Clear();
    public ArrayList<String> getLogs(long max);
    public void Reload();
    public void Disable();
}
