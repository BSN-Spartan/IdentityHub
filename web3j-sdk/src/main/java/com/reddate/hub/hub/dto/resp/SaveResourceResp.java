// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class SaveResourceResp extends HubData implements Serializable {

  private String url;

  private String encryptKey;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEncryptKey() {
    return encryptKey;
  }

  public void setEncryptKey(String encryptKey) {
    this.encryptKey = encryptKey;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, SaveResourceResp.class);
  }
}
