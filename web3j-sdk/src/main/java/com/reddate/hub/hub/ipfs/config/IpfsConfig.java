// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.ipfs.config;

import java.io.Serializable;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;

public class IpfsConfig implements Serializable {

  private String mmultiaddr;

  private CryptoType cryptoType;

  public String getMmultiaddr() {
    return mmultiaddr;
  }

  public CryptoType getCryptoType() {
    return cryptoType;
  }

  public static IpfsConfig parseConfigInfo(CryptoType cryptoType, String mmultiaddr) {
    IpfsConfig config = new IpfsConfig();
    if (mmultiaddr == null || mmultiaddr.trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.IPFS_NUTI_ADDRE.getCode(), ErrorMessage.IPFS_NUTI_ADDRE.getMessage());
    }
    config.mmultiaddr = mmultiaddr;
    if (cryptoType == null) {
      config.cryptoType = CryptoType.ECDSA;
    } else {
      config.cryptoType = cryptoType;
    }

    return config;
  }
}
