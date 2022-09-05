// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.util.http;

import java.io.Serializable;

public class RequestParam<T> implements Serializable {

  private String uid;

  private String userPublicKey;

  private T data;

  private String clientPublicKey;

  public RequestParam(String uid, String userPublicKey, T data, String clientPublicKey) {
    this.uid = uid;
    this.data = data;
    this.userPublicKey = userPublicKey;
    this.clientPublicKey = clientPublicKey;
  }

  public String getUid() {
    return uid;
  }

  public T getData() {
    return data;
  }

  public String getUserPublicKey() {
    return userPublicKey;
  }

  public String getClientPublicKey() {
    return clientPublicKey;
  }
}
