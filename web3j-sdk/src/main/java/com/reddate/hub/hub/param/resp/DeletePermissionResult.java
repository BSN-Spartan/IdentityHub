// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.param.resp;

public class DeletePermissionResult {

  private boolean succes;

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
}
