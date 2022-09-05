// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.dto;

import java.io.Serializable;

public class SyncDataResp implements Serializable {

  private String hubId;

  private Integer code;

  private String msg;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getHubId() {
    return hubId;
  }

  public void setHubId(String hubId) {
    this.hubId = hubId;
  }
}
