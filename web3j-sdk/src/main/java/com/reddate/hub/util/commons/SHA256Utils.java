// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.util.commons;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * SHA256 help class
 *
 * @author shaopengfei
 */
public class SHA256Utils {

  public static byte[] getSHA256(String str) {
    byte[] encode = {};
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(str.getBytes("UTF-8"));
      encode = messageDigest.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return encode;
  }

  public static String getSHA256String(String str) {
    String encodeStr = "";
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(str.getBytes("UTF-8"));
      encodeStr = byte2Hex(messageDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return encodeStr;
  }

  private static String byte2Hex(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      String temp = Integer.toHexString(bytes[i] & 0xFF);
      if (temp.length() == 1) {
        stringBuffer.append("0");
      }
      stringBuffer.append(temp);
    }
    return stringBuffer.toString();
  }
}
