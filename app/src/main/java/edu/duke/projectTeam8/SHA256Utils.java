package edu.duke.projectTeam8;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SHA256Utils {

    // The latest standard is used. None of the parameter here should be altered
    private static final String algorithm_name = "SHA-256";
    private static final SecureRandom secureRand = new SecureRandom();
    private static final int saltSize = 16;
    private static final Charset inputCharSet = StandardCharsets.UTF_8;

    /**
     * This function generate a new random String as salt when called.
     * The salt String should be stored at server together with target hash.
     * The salt String will be sent to the user during login.
     *
     * @return the randomly generated salt String
     */
    @Nonnull
    public static String makeSaltStr() {
        byte[] salt = new byte[saltSize];
        secureRand.nextBytes(salt);
        // Simply send bytes as Str, encoding doesn't matter
        return Base64.getEncoder().encodeToString(salt);
    }

    @Nonnull
    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * @param password the original password decided by the user
     * @param saltStr  randomly generated salt saved at the server sent to user
     * @return the salted SHA256sum of the user password
     * @throws NullPointerException     in case the password entered is null
     */

    public static String hashPassword(String password, String saltStr)
            throws NullPointerException {
        if (password == null)
            throw new NullPointerException();
        return hashPassword(password.getBytes(inputCharSet), saltStr);
    }


    public static String hashPassword(byte[] rawPasswordBytes, String saltStr) {
        try {

            MessageDigest md = MessageDigest.getInstance(algorithm_name);
            md.reset();
            md.update(decodeBase64(saltStr));
            byte[] hashedPassword = md.digest(rawPasswordBytes);
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
    }
}
