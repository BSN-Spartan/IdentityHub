// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;

import com.reddate.hub.hub.dto.HubData;

public class DeletePermissionResp extends HubData implements Serializable {

  private boolean succes = false;

  private String message;

  public boolean isSucces() {
    return succes;
  }

  public void setSucces(boolean succes) {
    this.succes = succes;
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
    return super.paseFromJsonString(data, DeletePermissionResp.class);
  }
}
