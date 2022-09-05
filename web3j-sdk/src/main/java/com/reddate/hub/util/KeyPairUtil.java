// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.Security;

public class KeyPairUtil {

  public static final String type = "Secp256k1";

  static {
    Security.addProvider(new BouncyCastleProvider());
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
    return false;
  }

  private static String secp256k1Sign(String rawData, BigInteger privateKey) {
    return null;
  }

  private static byte[] base64Encode(byte[] nonBase64Bytes) {
    return org.bouncycastle.util.encoders.Base64.encode(nonBase64Bytes);
  }

  private static byte[] base64Decode(byte[] base64Bytes) {
    return org.bouncycastle.util.encoders.Base64.decode(base64Bytes);
  }

  public static String sign2(String message, String privateKey) {
    return null;
  }


  public static boolean verify2(String message, String publicKey, String signValue) {
    return false;
  }

}
