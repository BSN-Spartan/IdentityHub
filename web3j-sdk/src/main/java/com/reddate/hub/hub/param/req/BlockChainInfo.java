// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.param.req;

import java.io.Serializable;

public class BlockChainInfo implements Serializable {

  private String addrId;

  private String address;

  private String publicKey;

  private String chainType;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getChainType() {
    return chainType;
  }

  public void setChainType(String chainType) {
    this.chainType = chainType;
  }

  public String getAddrId() {
    return addrId;
  }

  public void setAddrId(String addrId) {
    this.addrId = addrId;
  }
}
