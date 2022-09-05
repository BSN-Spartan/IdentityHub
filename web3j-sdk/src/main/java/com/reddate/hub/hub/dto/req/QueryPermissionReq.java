// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class QueryPermissionReq extends HubData implements Serializable {

  private String flag;

  private String grantUid;

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getGrantUid() {
    return grantUid;
  }

  public void setGrantUid(String grantUid) {
    this.grantUid = grantUid;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryPermissionReq.class);
  }
}
