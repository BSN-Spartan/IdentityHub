// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;
import java.util.List;

import com.reddate.hub.hub.dto.HubData;

/** Query resource owner granted permission response result description data structure */
public class QueryGantPermissionResp extends HubData implements Serializable {

  /** The grant permission information list */
  private List<GrantPermissionInfo> authList;

  public List<GrantPermissionInfo> getAuthList() {
    return authList;
  }

  public void setAuthList(List<GrantPermissionInfo> authList) {
    this.authList = authList;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryGantPermissionResp.class);
  }

  @Override
  public String toString() {
    return "QueryPermissionResp [permissionList=" + authList + "]";
  }
}
