// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class RegisterHubReq extends HubData implements Serializable {

  private String publicKey;

  private String uid;
  
  private Integer cryptoType;

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public Integer getCryptoType() {
	return cryptoType;
  }

  public void setCryptoType(Integer cryptoType) {
	this.cryptoType = cryptoType;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
    //    return publicKey;
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, RegisterHubReq.class);
    //    RegisterHubReq connHubReq = new RegisterHubReq();
    //    connHubReq.setPublicKey(data);
    //    return connHubReq;
  }
}
