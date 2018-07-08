package sk.crafting.connectionlogger.session;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Session {

    private String hashHex;
    private byte[] hash;

    public Session() {
        String salt = String.valueOf(System.nanoTime());
        salt = salt.substring(salt.length() - 4, salt.length() - 1);
        this.hash = DigestUtils.md5(System.currentTimeMillis() + salt);
        this.hashHex = Hex.encodeHexString(hash);
    }

    public byte[] getHash() {
        return hash;
    }

    public String getHashHex() {
        return hashHex;
    }

}
