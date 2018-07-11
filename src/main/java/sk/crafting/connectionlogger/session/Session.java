package sk.crafting.connectionlogger.session;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Session {

    private String hashHex;
    private String shortHashHex;
    private byte[] hash;

    public Session() {
        String salt = String.valueOf(System.nanoTime());
        if (salt.length() > 8) {
            salt = salt.substring(0, 7);
        }
        this.hash = DigestUtils.md5(System.currentTimeMillis() + salt);
        this.hashHex = Hex.encodeHexString(hash);
        this.shortHashHex = hashHex.substring(0, 9);
    }

    public byte[] getHash() {
        return hash;
    }

    public String getHashHex() {
        return hashHex;
    }

    public String getShortHashHex() {
        return shortHashHex;
    }
}
