// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.reddate.hub.hub.param.req.UsedFlag;

/** Query resource owner granted permission information description data structure */
public class GrantPermissionInfo implements Serializable {

  /** Granted resource url */
  private String url;

  /** granted permission */
  private String grant;

  /** granted permission status */
  private Integer status;

  /** granted permission create time */
  private LocalDateTime createTime;

  /** granted permission read time */
  private LocalDateTime readTime;

  /** granted permission used flag */
  private UsedFlag flag;

  /** resource owner user id */
  private String ownerUid;

  /** granted resource ciphertext key */
  private String key;

  /** resource owner ciphertext key */
  private String ownerKey;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getOwnerUid() {
    return ownerUid;
  }

  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
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
