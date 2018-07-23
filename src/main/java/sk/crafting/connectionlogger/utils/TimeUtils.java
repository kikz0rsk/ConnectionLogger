package sk.crafting.connectionlogger.utils;

import java.text.SimpleDateFormat;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public class TimeUtils
{

    public static final String DEFAULT_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String DATABASE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
    private static final SimpleDateFormat DATABASE_TIME_FORMATTER = new SimpleDateFormat(DATABASE_TIME_FORMAT);

    public static String format(long time) {
        return TIME_FORMATTER.format(time);
    }
    public static String databaseFormat(long time) {
        return DATABASE_TIME_FORMATTER.format(time);
    }

}
