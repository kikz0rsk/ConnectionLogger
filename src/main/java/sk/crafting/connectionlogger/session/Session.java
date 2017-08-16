package sk.crafting.connectionlogger.session;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Session {

    private String hashHex;
    private byte[] hash;

    public Session(byte[] hash) {
        this.hash = hash;
        this.hashHex = Hex.encodeHexString(hash);
    }

    public byte[] getHash() {
        return hash;
    }

    public String getHashHex() {
        return hashHex;
    }

}
