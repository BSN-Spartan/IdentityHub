// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.pojo;

import java.io.Serializable;

import com.reddate.hub.constant.CryptoType;

public class KeyPair implements Serializable {

  private String publicKey;

  private String privateKey;

  private CryptoType type;

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public CryptoType getType() {
    return type;
  }

  public void setType(CryptoType type) {
    this.type = type;
  }
}
