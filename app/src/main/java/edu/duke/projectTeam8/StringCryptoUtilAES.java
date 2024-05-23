package edu.duke.projectTeam8;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class StringCryptoUtilAES {
  private final int ivLength;
  private final SecretKey secretKey;
  private final Cipher cipher;

  private StringCryptoUtilAES(int keyLength, int iterationCount, int ivLength,
      String password, String protocolName)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
    this.ivLength = ivLength;
    this.cipher = Cipher.getInstance(protocolName);
    this.secretKey = StringCryptoUtilAES.getKeyFromPassword(password, iterationCount, keyLength);

  }

  public StringCryptoUtilAES(String password)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
    this(256, 65536, 16, password, "AES/CBC/PKCS5Padding");

  }

  private static SecretKey getKeyFromPassword(String password, int iterationCount, int keyLength)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    int saltLength = password.length() / 3;
    byte[] saltData = password.substring(0, saltLength).getBytes();
    char[] passwordData = password.substring(saltLength).toCharArray();

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(passwordData, saltData, iterationCount, keyLength);
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

  }

  private byte[] generateIvParam() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] iv = new byte[this.ivLength];
    secureRandom.nextBytes(iv);
    return iv;
  }

  public String doEncryption(String strToEncrypt) throws IllegalBlockSizeException,
      BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
    byte[] iv = generateIvParam();
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    ;

    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

    byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes());
    byte[] encryptedData = new byte[iv.length + cipherText.length];
    System.arraycopy(iv, 0, encryptedData, 0, iv.length);
    System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

    return Base64.getEncoder().encodeToString(encryptedData);
  }

  public String doDecryption(String strToDecrypt) throws IllegalBlockSizeException,
      BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
      InvalidKeyException, UnsupportedEncodingException {

    byte[] encryptedData = Base64.getDecoder().decode(strToDecrypt);

    byte[] iv = new byte[ivLength];
    System.arraycopy(encryptedData, 0, iv, 0, ivLength);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

    byte[] cipherText = new byte[encryptedData.length - ivLength];
    System.arraycopy(encryptedData, ivLength, cipherText, 0, cipherText.length);

    byte[] decryptedText = cipher.doFinal(cipherText);
    return new String(decryptedText);

  }

}
