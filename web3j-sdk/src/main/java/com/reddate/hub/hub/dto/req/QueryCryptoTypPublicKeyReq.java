// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.req;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class QueryCryptoTypPublicKeyReq extends HubData implements Serializable {

  private String regUserId;
	
  private String publicKey;

  public String getRegUserId() {
	return regUserId;
  }

  public void setRegUserId(String regUserId) {
	this.regUserId = regUserId;
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
    return super.paseFromJsonString(data, QueryCryptoTypPublicKeyReq.class);
  }
}
