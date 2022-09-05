// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub;

import com.reddate.hub.constant.CryptoType;

public interface HubSecurityHandler {

  public String getPublicKey();

  public String getUid();

  public String encrypt(String content);

  public String decrypt(String content);

  public String sign(String content);

  public void setExtraMsg(String msg, String sign);

  public String getExtraMsg();

  public static String pasrseExtraMsg(String msg, String sign) {
    if (msg == null || msg.trim().isEmpty()) {
      throw new RuntimeException("the extra message is empty");
    }
    if (sign == null || sign.trim().isEmpty()) {
      throw new RuntimeException("the extra message signature is empty");
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append("{\"");
    buffer.append(msg);
    buffer.append("\",\"");
    buffer.append(sign);
    buffer.append("\"}");
    return buffer.toString();
  }

  public CryptoType getCryptoType();
}
