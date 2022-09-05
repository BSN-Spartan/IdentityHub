// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HubMongoRecord implements Serializable {

  private String tableName;

  private List<FieldInfo> fields = new ArrayList<>();

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public List<FieldInfo> getFields() {
    return fields;
  }

  public void setFields(List<FieldInfo> fields) {
    this.fields = fields;
  }
}
