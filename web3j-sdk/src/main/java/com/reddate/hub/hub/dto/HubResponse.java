// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class HubResponse<T extends HubData> implements Serializable {

  public static final int SUCCECSS = 200;

  private Integer code;

  private String encptData;

  private T data;

  private String msg;

  public int getCode() {
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

  public void setData(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public String getEncptData() {
    return encptData;
  }

  public void setEncptData(String encptData) {
    this.encptData = encptData;
  }

  public String formatToString() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("code", code);
    map.put("msg", msg);
    if (data != null) {
      map.put("encptData", data.seriToString());
    }
    String dataStr = JSONObject.toJSONString(map);
    return dataStr;
  }

  public static <T extends HubData> HubResponse<T> parseFormString(
      String dataStr, Class<T> returnType) {
    Map<String, Object> map = JSONObject.parseObject(dataStr, Map.class);

    HubResponse<T> hubResponse = new HubResponse<>();
    Integer code = (Integer) map.get("code");
    String msg = (String) map.get("msg");
    String data = (String) map.get("encptData");
    hubResponse.setCode(code);
    hubResponse.setMsg(msg);
    if (data != null && data.trim().length() > 0) {
      T dataT = JSONObject.parseObject(data, returnType);
      hubResponse.setData(dataT);
    }
    return hubResponse;
  }
}
