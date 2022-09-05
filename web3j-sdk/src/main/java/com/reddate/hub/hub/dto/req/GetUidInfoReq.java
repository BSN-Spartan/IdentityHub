// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;
import java.util.List;

import com.reddate.hub.hub.dto.HubData;

public class GetUidInfoReq extends HubData implements Serializable {
  private String publicKey;

  private List<ResourceKeyInfo> resourceKeyList;

  public List<ResourceKeyInfo> getResourceKeyList() {
    return resourceKeyList;
  }

  public void setResourceKeyList(List<ResourceKeyInfo> resourceKeyList) {
    this.resourceKeyList = resourceKeyList;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, GetUidInfoReq.class);
  }
}
