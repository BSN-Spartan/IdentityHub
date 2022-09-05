// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.ipfs;

import com.reddate.hub.hub.ipfs.config.IpfsConfig;
import com.reddate.hub.hub.ipfs.param.resp.ResourceInfo;
import com.reddate.hub.hub.ipfs.param.resp.UploadResource;
import com.reddate.hub.hub.ipfs.service.IpfsResourceService;
import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;

public class IpfsConnection {

  private IpfsResourceService ipfsResourceService;

  public IpfsConnection(CryptoType cryptoType, String multiaddr) {
    IpfsConfig ipfsConfig = IpfsConfig.parseConfigInfo(cryptoType, multiaddr);
    this.ipfsResourceService = new IpfsResourceService(ipfsConfig);
  }

  public UploadResource uploadResource(String content, String privateKey) {
    UploadResource saveResourceResp = null;
    try {
      saveResourceResp = ipfsResourceService.uploadResource(content, privateKey);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return saveResourceResp;
  }

  public ResourceInfo downloadResource(String multihash) {
    ResourceInfo saveResourceResp = null;
    try {
      saveResourceResp = ipfsResourceService.downloadResource(multihash);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return saveResourceResp;
  }
}
