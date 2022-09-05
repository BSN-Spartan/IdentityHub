// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.ipfs.service;

import com.reddate.hub.hub.ipfs.config.IpfsConfig;

public class IpfsService {

  protected IpfsConfig ipfsConfig;

  public IpfsService(IpfsConfig ipfsConfig) {
    if (ipfsConfig != null) {
      this.ipfsConfig = ipfsConfig;
    }
  }

  public IpfsConfig getIpfsConfig() {
    return ipfsConfig;
  }
}
