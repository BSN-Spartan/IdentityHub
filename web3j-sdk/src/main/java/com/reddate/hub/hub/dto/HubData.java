// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto;

import com.alibaba.fastjson.JSONObject;

public abstract class HubData {

  protected String uid;

  public abstract String seriToString();

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public abstract Object paseFromSeriString(String data);

  protected String toJsonString(HubData data) {
    String jsonString = JSONObject.toJSONString(data);
    return jsonString;
  }

  protected <T extends HubData> T paseFromJsonString(String data, Class<T> classes) {
    T obj = JSONObject.parseObject(data, classes);
    return obj;
  }
}
