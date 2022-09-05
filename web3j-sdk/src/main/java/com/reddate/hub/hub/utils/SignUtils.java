// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.utils;

import cn.hutool.core.codec.Base64;
import com.reddate.hub.hub.utils.ECC.Decrypt;
import com.reddate.hub.hub.utils.ECC.Encrypt;
import com.reddate.hub.hub.utils.ECC.KeyPair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.Security;

public class SignUtils {

  public static final String type = "Secp256k1";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static KeyPair createKey() throws Exception {
    KeyPair keyPair = new KeyPair();
    keyPair.setType(type);
    return keyPair;
  }

  public static String getPublicKey(String privateKey) {
    try {
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("invalid privateKey");
    }
  }

  /**
   * Sign the message according to the private key
   *
   * @param message
   * @param privateKey
   * @author shaopengfei
   */
  public static String sign(String message, String privateKey, String publicKey) {
    return sign(message, privateKey);
  }

  public static String sign(String message, String privateKey) {
    return null;
  }

  /**
   * Verify the signature of the message according to the public key
   *
   * @param message
   * @param publicKey
   * @param signValue
   * @author shaopengfei
   */
  public static boolean verify(String message, String publicKey, String signValue) {
    boolean fiscoVerify = false;
    try {

    } catch (Exception e) {
      fiscoVerify = false;
    }
    if (fiscoVerify) {
      return fiscoVerify;
    }

    boolean ethverify = false;
    try {
      ethverify = EthSignUtil.verify(message, publicKey, signValue);
    } catch (Exception e) {
      ethverify = false;
    }

    return (fiscoVerify || ethverify);
  }

  private static String secp256k1Sign(String rawData, BigInteger privateKey) {
    return null;
  }

  private static boolean verifySecp256k1Signature(
      String rawData, String signatureBase64, BigInteger publicKey) {
    if (rawData == null) {
      return false;
    }
    return false;
  }

  private static byte[] base64Encode(byte[] nonBase64Bytes) {
    return org.bouncycastle.util.encoders.Base64.encode(nonBase64Bytes);
  }

  private static byte[] base64Decode(byte[] base64Bytes) {
    return org.bouncycastle.util.encoders.Base64.decode(base64Bytes);
  }

  public static String encrypt(String data, String publicKey) throws Exception {
    Encrypt encrypts = new Encrypt(new BigInteger(publicKey));
    byte[] bytes = encrypts.encrypt(data.getBytes());
    String enCode = Base64.encode(bytes);
    return enCode;
  }

  public static String decrypt(String data, String privateKey) throws Exception {
    Decrypt decrypt = new Decrypt(new BigInteger(privateKey));
    byte[] bytes = decrypt.decrypt(Base64.decode(data));
    String deCode = new String(bytes);
    return deCode;
  }

  public static String sign2(String message, String privateKey) {
    return null;
  }

  public static boolean verify2(String message, String publicKey, String signValue) {
    return false;
  }

}
