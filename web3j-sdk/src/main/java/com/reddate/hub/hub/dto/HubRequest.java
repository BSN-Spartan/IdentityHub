// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto;

import java.io.Serializable;

public class HubRequest<T extends HubData> implements Serializable {

  private T data;

  private String encptData;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
