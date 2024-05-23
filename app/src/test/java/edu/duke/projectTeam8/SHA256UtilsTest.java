package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

class SHA256UtilsTest {
    @Test
    void test_exceptions(){
        assertThrows(NullPointerException.class,
                ()->SHA256Utils.hashPassword("", null));
        assertThrows(NullPointerException.class,
                ()->SHA256Utils.hashPassword((byte[]) null, SHA256Utils.makeSaltStr()));
    }
    @Test
    void makeSaltStr() {
        for (int i = 0; i < 10; i++) {
            String randSalt = SHA256Utils.makeSaltStr();
            assertEquals(SHA256Utils.decodeBase64(randSalt).length, 16);
        }
    }

    @Test
    void hashPassword() throws NoSuchAlgorithmException {
        String randSalt = SHA256Utils.makeSaltStr();
        String hashed1 = SHA256Utils.hashPassword("", randSalt);
        String hashed2 = SHA256Utils.hashPassword(" ", randSalt);

        assertTrue(is256bits(hashed1));
        assertTrue(is256bits(hashed2));
        assertNotEquals(hashed1, hashed2);
        String hashed3 = SHA256Utils.hashPassword("随便", randSalt);
        assertTrue(is256bits(hashed3));
        assertThrows(NullPointerException.class, () -> SHA256Utils.hashPassword((byte[]) null, randSalt));
        String hashed4 = SHA256Utils.hashPassword("not a bad password", randSalt);
        assertTrue(is256bits(hashed4));
        assertNotEquals(hashed4, hashed2);
        assertThrows(NullPointerException.class, () -> SHA256Utils.hashPassword((String) null, randSalt));

    }

    private boolean is256bits(String str) {
        return SHA256Utils.decodeBase64(str).length * 8 == 256;
    }
}