// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;
import java.util.List;

import com.reddate.hub.hub.dto.HubData;

public class QueryPermissionResp extends HubData implements Serializable {

  private List<PermissionInfo> authList;

  public List<PermissionInfo> getAuthList() {
    return authList;
  }

  public void setAuthList(List<PermissionInfo> authList) {
    this.authList = authList;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryPermissionResp.class);
  }

  @Override
  public String toString() {
    return "QueryPermissionResp [permissionList=" + authList + "]";
  }
}
