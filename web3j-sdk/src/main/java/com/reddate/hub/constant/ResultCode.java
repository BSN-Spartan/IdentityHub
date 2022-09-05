// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.constant;

public class ResultCode {

  private Integer code;
  private String chMessage;
  private String enMessage;

  public ResultCode(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.enMessage = errorCode.getEnMessage();
  }

  public ResultCode(ErrorCode errorCode, String enMessage) {
    this.code = errorCode.getCode();
    this.enMessage = enMessage + errorCode.getEnMessage();
  }

  public ResultCode(Integer code, String enMessage) {
    this.code = code;
    this.enMessage = enMessage;
  }


  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getChMessage() {
    return chMessage;
  }

  public void setChMessage(String chMessage) {
    this.chMessage = chMessage;
  }

  public String getEnMessage() {
    return enMessage;
  }

  public void setEnMessage(String enMessage) {
    this.enMessage = enMessage;
  }

  public ResultCode editEnMessage(String enMessage) {
    this.setEnMessage(enMessage + this.enMessage);
    return this;
  }
}
