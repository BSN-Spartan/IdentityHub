// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.reddate.hub.hub.param.req.UsedFlag;

public class PermissionInfo implements Serializable {

  private String url;

  private String grant;

  private String grantUid;

  private Integer status;

  private LocalDateTime createTime;

  private LocalDateTime readTime;

  private UsedFlag flag;

  private String uid;

  private String key;

  private String ownerKey;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getGrantUid() {
    return grantUid;
  }

  public void setGrantUid(String grantUid) {
    this.grantUid = grantUid;
  }

  public UsedFlag getFlag() {
    return flag;
  }

  public void setFlag(UsedFlag flag) {
    this.flag = flag;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getReadTime() {
    return readTime;
  }

  public void setReadTime(LocalDateTime readTime) {
    this.readTime = readTime;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getGrant() {
    return grant;
  }

  public void setGrant(String grant) {
    this.grant = grant;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getOwnerKey() {
    return ownerKey;
  }

  public void setOwnerKey(String ownerKey) {
    this.ownerKey = ownerKey;
  }
}
