package sk.crafting.connectionlogger.listeners;

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

}
