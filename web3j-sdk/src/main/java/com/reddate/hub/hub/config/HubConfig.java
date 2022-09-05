// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.config;

import java.io.Serializable;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;

public class HubConfig implements Serializable {

  private String hubUrl;

  private String hubPublicKey;

  CryptoType cryptoType;

  public String getHubUrl() {
    return hubUrl;
  }

  public void setHubUrl(String hubUrl) {
    this.hubUrl = hubUrl;
  }

  public String getHubPublicKey() {
    return hubPublicKey;
  }

  public void setHubPublicKey(String hubPublicKey) {
    this.hubPublicKey = hubPublicKey;
  }

  public CryptoType getCryptoType() {
    return cryptoType;
  }

  public void setCryptoType(CryptoType cryptoType) {
    this.cryptoType = cryptoType;
  }

  public static HubConfig parseConfigInfo(
      CryptoType cryptoType, String hubUrl, String hubPublicKey) {
    HubConfig config = new HubConfig();

    if (cryptoType == null) {
      config.setCryptoType(CryptoType.ECDSA);
    } else {
      config.setCryptoType(cryptoType);
    }

    if (hubUrl == null || hubUrl.trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.HUB_URL_EMPTY.getCode(), ErrorMessage.HUB_URL_EMPTY.getMessage());
    }
    config.setHubUrl(hubUrl);

    if (hubPublicKey == null || hubPublicKey.trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.HUB_PULIC_KEY_EMPTY.getCode(),
          ErrorMessage.HUB_PULIC_KEY_EMPTY.getMessage());
    }
    config.setHubPublicKey(hubPublicKey);

    return config;
  }
}
