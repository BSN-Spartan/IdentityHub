// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class AddBlockChainResp extends HubData implements Serializable {

  private Boolean restult = false;

  private String message;

  public Boolean getRestult() {
    return restult;
  }

  public void setRestult(Boolean restult) {
    this.restult = restult;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, AddBlockChainResp.class);
  }
}
