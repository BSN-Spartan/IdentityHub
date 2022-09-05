// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.protocol.response;

import com.reddate.hub.constant.ContractCode;
import com.reddate.hub.constant.ErrorCode;
import com.reddate.hub.constant.ResultCode;

/**
 * Return parameter public entity
 *
 * @param <T>
 */
public class ResponseData<T> {

  /** Common type return object */
  private T result;

  /** Error code */
  private Integer errorCode;

  /** Chinese error message */
  private String errorChMessage;

  /** English error message */
  private String errorEnMessage;

  /** Instantiate default response message */
  public ResponseData() {
    this.setErrorCode(ErrorCode.SUCCESS);
  }

  /** Instantiate the corresponding code according to the parameters */
  public ResponseData(T result, ErrorCode errorCode) {
    this.result = result;
    if (errorCode != null) {
      this.errorCode = errorCode.getCode();
      this.errorEnMessage = errorCode.getEnMessage();
    }
  }

  public ResponseData(T result, ResultCode resultCode) {
    this.result = result;
    if (resultCode != null) {
      this.errorCode = resultCode.getCode();
      this.errorEnMessage = resultCode.getEnMessage();
    }
  }

  public ResponseData(T result, ErrorCode errorCode, ContractCode contractCode) {
    this.result = result;
    if (errorCode != null) {
      this.errorCode = errorCode.getCode();
      this.errorEnMessage = errorCode.getEnMessage();
    }
    if (contractCode != null) {
      if (this.errorCode == null) {
        this.errorCode = Integer.parseInt(contractCode.getCode());
      }
      if (this.errorChMessage == null) {
        this.errorChMessage = contractCode.getEnMessage();
      } else {
        this.errorChMessage += contractCode.getEnMessage();
      }
      if (this.errorEnMessage == null) {
        this.errorEnMessage = contractCode.getEnMessage();
      } else {
        this.errorEnMessage += contractCode.getEnMessage();
      }
    }
  }

  /** Fill in response error entity */
  public void setErrorCode(ErrorCode errorCode) {
    if (errorCode != null) {
      this.errorCode = errorCode.getCode();
      this.errorEnMessage = errorCode.getEnMessage();
    }
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorChMessage() {
    return errorChMessage;
  }

  public void setErrorChMessage(String errorChMessage) {
    this.errorChMessage = errorChMessage;
  }

  public String getErrorEnMessage() {
    return errorEnMessage;
  }

  public void setErrorEnMessage(String errorEnMessage) {
    this.errorEnMessage = errorEnMessage;
  }
}
