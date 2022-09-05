// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.param.req;

import java.io.Serializable;

public class AddPermission implements Serializable {

  private String url;

  private Operation grant;

  private String grantUid;

  private String grantPublicKey;

  private String grantEncryptKey;

  public String getGrantUid() {
    return grantUid;
  }

  public void setGrantUid(String grantUid) {
    this.grantUid = grantUid;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Operation getGrant() {
    return grant;
  }

  public void setGrant(Operation grant) {
    this.grant = grant;
  }

  public String getGrantPublicKey() {
    return grantPublicKey;
  }

  public void setGrantPublicKey(String grantPublicKey) {
    this.grantPublicKey = grantPublicKey;
  }

  public String getGrantEncryptKey() {
    return grantEncryptKey;
  }

  public void setGrantEncryptKey(String grantEncryptKey) {
    this.grantEncryptKey = grantEncryptKey;
  }
}
