package sk.crafting.connectionlogger.session;

import org.apache.commons.codec.digest.DigestUtils;
import sk.crafting.connectionlogger.ConnectionLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionManager {

    private Path lockFile;
    private Session currentSession;

    public SessionManager() {

    }

    public void StartSession() {
        String salt = String.valueOf(System.nanoTime());
        salt = salt.substring(salt.length() - 4, salt.length() - 1);
        Session session = new Session(DigestUtils.sha256(System.currentTimeMillis() + salt));
        this.currentSession = session;
        lockFile = Paths.get(ConnectionLogger.getInstance().getDataFolder() + "/lock.lock");
        try {
            Files.createFile(lockFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CloseSession() {
        try {
            Files.delete(lockFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public Path getLockFile() {
        return lockFile;
    }

}
