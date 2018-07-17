package sk.crafting.connectionlogger.cache;

/**
 *
 * @author Red-Eye~kikz0r_sk
 */
public enum EventType
{

    CONNECT("Connect"), DISCONNECT("Disconnect");

    private final String msg;

    EventType(String msg)
    {
        this.msg = msg;
    }

    public String getMessage()
    {
        return msg;
    }

    public static EventType getEventTypeByString(String type) {
        for (EventType e : EventType.values()) {
            if(e.getMessage().equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }

}
