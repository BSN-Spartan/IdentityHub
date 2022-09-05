// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.util.commons;

import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * RipeMD160 help class
 *
 * @author shaopengfei
 */
public class RipeMDUtils {

  public static byte[] encodeRipeMD128(byte[] data) throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    MessageDigest md = MessageDigest.getInstance("RipeMD128");
    return md.digest(data);
  }

  public static String encodeRipeMD128Hex(byte[] data) throws Exception {
    byte[] b = encodeRipeMD128(data);
    return new String(Hex.encode(b));
  }

  public static byte[] encodeRipeMD160(byte[] data) throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    MessageDigest md = MessageDigest.getInstance("RipeMD160");
    return md.digest(data);
  }

  public static String encodeRipeMD160Hex(byte[] data) throws Exception {
    byte[] b = encodeRipeMD160(data);
    return new String(Hex.encode(b));
  }

  public static byte[] encodeRipeMD256(byte[] data) throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    MessageDigest md = MessageDigest.getInstance("RipeMD256");
    return md.digest(data);
  }

  public static String encodeRipeMD256Hex(byte[] data) throws Exception {
    byte[] b = encodeRipeMD256(data);
    return new String(Hex.encode(b));
  }

  public static byte[] encodeRipeMD320(byte[] data) throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    MessageDigest md = MessageDigest.getInstance("RipeMD320");
    return md.digest(data);
  }

  public static String encodeRipeMD320Hex(byte[] data) throws Exception {
    byte[] b = encodeRipeMD320(data);
    return new String(Hex.encode(b));
  }

  public static byte[] initHmacRipeMD128Key() throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacRipeMD128");
    SecretKey secretKey = keyGenerator.generateKey();
    return secretKey.getEncoded();
  }

  public static byte[] encodeHmacRipeMD128(byte[] data, byte[] key) throws Exception {
    if (data == null || data.length <= 0) {
      throw new Exception("invalid data");
    }
    if (key == null || key.length <= 0) {
      throw new Exception("invalid key");
    }
    Security.addProvider(new BouncyCastleProvider());
    SecretKey secretKey = new SecretKeySpec(key, "HmacRipeMD128");
    Mac mac = Mac.getInstance(secretKey.getAlgorithm());
    mac.init(secretKey);
    return mac.doFinal(data);
  }

  public static String encodeHmacRipeMD128Hex(byte[] data, byte[] key) throws Exception {
    if (key != null && key.length > 0) {
      byte[] b = encodeHmacRipeMD128(data, key);
      return new String(Hex.encode(b));
    }
    throw new Exception("invalid key");
  }

  public static byte[] initHmacRipeMD160Key() throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacRipeMD160");
    SecretKey secretKey = keyGenerator.generateKey();
    return secretKey.getEncoded();
  }

  public static byte[] encodeHmacRipeMD160(byte[] data, byte[] key) throws Exception {
    if (key == null || key.length == 0) {
      return null;
    }
    Security.addProvider(new BouncyCastleProvider());
    SecretKey secretKey = new SecretKeySpec(key, "HmacRipeMD160");
    Mac mac = Mac.getInstance(secretKey.getAlgorithm());
    mac.init(secretKey);
    return mac.doFinal(data);
  }

  public static String encodeHmacRipeMD160Hex(byte[] data, byte[] key) throws Exception {
    if (key == null || key.length == 0 || data == null || data.length == 0) {
      return null;
    }
    byte[] b = encodeHmacRipeMD160(data, key);
    return new String(Hex.encode(b));
  }
}
