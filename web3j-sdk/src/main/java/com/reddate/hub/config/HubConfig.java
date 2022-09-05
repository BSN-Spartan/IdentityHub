// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.config;

import java.math.BigInteger;

public class HubConfig {

  private String type;

  private String nodeUrl;

  private String accountPrivateKey;

  private BigInteger gasPrice;

  private BigInteger gasLimit;

  private Integer receiptRotationCount;

  private Long receiptWaitTime;

  private Long chainId;


  public Long getChainId() {
    return chainId;
  }

  public void setChainId(Long chainId) {
    this.chainId = chainId;
  }

  public Integer getReceiptRotationCount() {
    return receiptRotationCount;
  }

  public void setReceiptRotationCount(Integer receiptRotationCount) {
    this.receiptRotationCount = receiptRotationCount;
  }

  public Long getReceiptWaitTime() {
    return receiptWaitTime;
  }

  public void setReceiptWaitTime(Long receiptWaitTime) {
    this.receiptWaitTime = receiptWaitTime;
  }

  public BigInteger getGasLimit() {
    return gasLimit;
  }

  public void setGasLimit(BigInteger gasLimit) {
    this.gasLimit = gasLimit;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNodeUrl() {
    return nodeUrl;
  }

  public void setNodeUrl(String nodeUrl) {
    this.nodeUrl = nodeUrl;
  }

  public String getAccountPrivateKey() {
    return accountPrivateKey;
  }

  public void setAccountPrivateKey(String accountPrivateKey) {
    this.accountPrivateKey = accountPrivateKey;
  }

  public BigInteger getGasPrice() {
    return gasPrice;
  }

  public void setGasPrice(BigInteger gasPrice) {
    this.gasPrice = gasPrice;
  }
}
