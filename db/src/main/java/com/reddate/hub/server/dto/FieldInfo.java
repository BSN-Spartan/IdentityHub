// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.dto;

import java.io.Serializable;

public class FieldInfo implements Serializable {

  private String name;

  private String val;

  private Integer type;

  public FieldInfo() {
    super();
  }

  public FieldInfo(String name, String val, Integer type) {
    super();
    this.name = name;
    this.val = val;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = val;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }
}
