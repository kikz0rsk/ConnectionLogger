package sk.crafting.connectionlogger.session;

import sk.crafting.connectionlogger.ConnectionLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionManager {

    private static SessionManager instance;

    private Path lockFile = Paths.get(ConnectionLogger.getInstance().getDataFolder() + "/session.lock");
    private boolean sessionRunning;
    private Session session;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void startSession() {
        Session session = new Session();
        this.session = session;

        if(Files.exists(lockFile) && !sessionRunning) {
            ConnectionLogger.getInstance().getLogger().warning("Previous session has not ended up correctly (server might have crashed). This means logs from previous session may be incomplete.");
        } else {
            try {
                Files.createFile(lockFile);
            } catch (IOException e) {
                ConnectionLogger.getInstance().getLogger().warning("Failed to create temporary session file: " + e.getMessage());
            }
        }
        ConnectionLogger.getInstance().getLogger().info("Started new session with ID " + session.getHashHex());
        sessionRunning = true;
    }

    public void closeSession() {
        try {
            Files.delete(lockFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sessionRunning = false;
    }

    public Session getSession() {
        return session;
    }

}
