// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ResourceHistoryInfo implements Serializable {

  private String operationUid;

  private String ownerUid;

  private String operation;

  private String content;

  private String url;

  private String key;

  private LocalDateTime operationTime;

  public String getOperationUid() {
    return operationUid;
  }

  public void setOperationUid(String operationUid) {
    this.operationUid = operationUid;
  }

  public String getOwnerUid() {
    return ownerUid;
  }

  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public LocalDateTime getOperationTime() {
    return operationTime;
  }

  public void setOperationTime(LocalDateTime operationTime) {
    this.operationTime = operationTime;
  }
}
