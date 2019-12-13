package de.fuzzlemann.ucutils.utils.api;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class AuthHash {

    private static final SecureRandom RANDOM = new SecureRandom();
    private final String username;
    private final long currentTime;
    private final long randomLong;
    private final String hash;

    public AuthHash() {
        username = AbstractionLayer.getPlayer().getName();
        currentTime = System.currentTimeMillis();
        randomLong = RANDOM.nextLong();
        hash = hash(username + currentTime + randomLong);
    }

    private static String hash(String str) {
        try {
            byte[] digest = digest(str, "SHA-1");
            return new BigInteger(digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] digest(String str, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        return md.digest(strBytes);
    }

    public String getUsername() {
        return username;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public long getRandomLong() {
        return randomLong;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthHash.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("currentTime=" + currentTime)
                .add("randomLong=" + randomLong)
                .add("hash='" + hash + "'")
                .toString();
    }
}
