// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.reddate.hub.constant.CryptoType;

@ConfigurationProperties(prefix = "hub")
public class HubConfigInfo {

  private String configFileName;

  private String privateKey;

  private String publicKey;

  private CryptoType cryptoType;

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getConfigFileName() {
    return configFileName;
  }

  public void setConfigFileName(String configFileName) {
    this.configFileName = configFileName;
  }

  public CryptoType getCryptoType() {
    return cryptoType;
  }

  public void setCryptoType(CryptoType cryptoType) {
    this.cryptoType = cryptoType;
  }
}
