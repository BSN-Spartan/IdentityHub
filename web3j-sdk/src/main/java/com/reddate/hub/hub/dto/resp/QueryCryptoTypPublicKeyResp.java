// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class QueryCryptoTypPublicKeyResp extends HubData implements Serializable {

  private Integer type;

  public Integer getType() {
	return type;
  }

  public void setType(Integer type) {
	this.type = type;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryCryptoTypPublicKeyResp.class);
  }
}
