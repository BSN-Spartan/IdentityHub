// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class TransferOwnerReq extends HubData implements Serializable {

  private String url;

  private String newOwnerUid;

  private String newKey;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getNewKey() {
    return newKey;
  }

  public void setNewKey(String newKey) {
    this.newKey = newKey;
  }

  public String getNewOwnerUid() {
    return newOwnerUid;
  }

  public void setNewOwnerUid(String newOwnerUid) {
    this.newOwnerUid = newOwnerUid;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, TransferOwnerReq.class);
  }
}
