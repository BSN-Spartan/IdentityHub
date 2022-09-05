// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

/** Query resource owner granted permission request parameter description data structure */
public class QueryGrantedPermissionReq extends HubData implements Serializable {

  /** Permission has used flag: 0 - has used , 1- not use */
  private String flag;

  /** the resource owner user id */
  private String ownerUid;

  /** the grant operation name */
  private String grant;

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getOwnerUid() {
    return ownerUid;
  }

  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
  }

  public String getGrant() {
    return grant;
  }

  public void setGrant(String grant) {
    this.grant = grant;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryGrantedPermissionReq.class);
  }
}
