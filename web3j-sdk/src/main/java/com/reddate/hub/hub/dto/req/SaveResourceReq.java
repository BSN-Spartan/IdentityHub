// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class SaveResourceReq extends HubData implements Serializable {

  private String enContent;

  private String enKey;

  private String ownerUid;

  private String grant;

  private String url;

  public String getEnContent() {
    return enContent;
  }

  public void setEnContent(String enContent) {
    this.enContent = enContent;
  }

  public String getEnKey() {
    return enKey;
  }

  public void setEnKey(String enKey) {
    this.enKey = enKey;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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
    return super.paseFromJsonString(data, SaveResourceReq.class);
  }
}
