// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class QueryBlockChainInfoReq extends HubData implements Serializable {

  private String address;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryBlockChainInfoReq.class);
  }
}
