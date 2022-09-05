// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.ipfs.IpfsConnection;
import com.reddate.hub.hub.ipfs.param.resp.ResourceInfo;
import com.reddate.hub.hub.ipfs.param.resp.UploadResource;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.util.Secp256Util;
import com.reddate.hub.hub.constant.ErrorMessage;

public class IpfsClient {

  private IpfsConnection ipfsConnection;

  public IpfsClient(String multiaddr) {
    ipfsConnection = new IpfsConnection(CryptoType.ECDSA, multiaddr);
  }

  public IpfsClient(CryptoType cryptoType, String multiaddr) {
    ipfsConnection = new IpfsConnection(cryptoType, multiaddr);
  }

  public UploadResource saveResource(String content, String privateKey) {
    return ipfsConnection.uploadResource(content, privateKey);
  }

  public ResourceInfo getResource(String multihash) {
    return ipfsConnection.downloadResource(multihash);
  }

  public static String decrypt(
      CryptoType cryptoType, String content, String encptyKey, String privateKey) {
    String key = null;
    try {
      key = Secp256Util.decrypt(cryptoType, encptyKey, privateKey);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.DECRYPT_FAILED.getCode(),
          ErrorMessage.DECRYPT_FAILED.getMessage() + e.getMessage());
    }
    try {
      return AesUtils.decrypt(content, key);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.DECRYPT_FAILED.getCode(),
          ErrorMessage.DECRYPT_FAILED.getMessage() + e.getMessage());
    }
  }
}
