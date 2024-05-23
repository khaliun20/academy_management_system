package edu.duke.projectTeam8;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringCryptoUtilAESTest {

    @Test
    public void test_cycle_consistency()
            throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String plainText = "<studentID>Hello World!\nI love JAVA</>";

        String passwd = "ThisisARandomKey";

        StringCryptoUtilAES cryptoUtil = new StringCryptoUtilAES(passwd);

        String cypherText = cryptoUtil.doEncryption(plainText);

        String decryptedText = cryptoUtil.doDecryption(cypherText);
        assertEquals(plainText, decryptedText);

        String cypString2 = cryptoUtil.doEncryption(plainText);
        assertNotEquals(cypherText, cypString2);
    }


    @Test
    public void test_2() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            UnsupportedEncodingException {
        String plainText0 = "<enrollment>\n\t<student>\n\t\t<first name>John</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>john@aol.com</email>\n\t\t<netID>js101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Tardy</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n\t<student>\n\t\t<first name>Mike</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>mike@aol.com</email>\n\t\t<netID>ms101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Attended</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n</enrollment>";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++)
            sb.append(plainText0);
        String plainText = sb.toString();
        String passwd = "ThisisARandomKey";

        StringCryptoUtilAES cryptoUtil = new StringCryptoUtilAES(passwd);

        String cypherText = cryptoUtil.doEncryption(plainText);
//        System.out.println(cypherText);
        String decryptedText = cryptoUtil.doDecryption(cypherText);
        assertEquals(plainText, decryptedText);

    }

}
