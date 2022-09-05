// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.ipfs.service;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.ipfs.config.IpfsConfig;
import com.reddate.hub.hub.ipfs.param.resp.ResourceInfo;
import com.reddate.hub.hub.ipfs.param.resp.UploadResource;
import com.reddate.hub.hub.ipfs.utils.IpfsUtls;

//import io.ipfs.api.MerkleNode;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class IpfsResourceService extends IpfsService {

  public IpfsResourceService(IpfsConfig ipfsConfig) {
    super(ipfsConfig);
  }

  public UploadResource uploadResource(String content, String privateKey) throws Exception {
    return null;
  }

  public ResourceInfo downloadResource(String multihash) throws Exception {
    if (StringUtils.isEmpty(multihash)) {
      throw new IdentityHubException(
          ErrorMessage.IPFS_FILE_HASH_EMPTY.getCode(),
          ErrorMessage.IPFS_FILE_HASH_EMPTY.getMessage());
    }

    Map<String, Object> dataMap = null;
    try {
      dataMap = IpfsUtls.getFileToIpfs(multihash, ipfsConfig, Map.class);
    } catch (Exception e1) {
      e1.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.QUERY_RESOURCE_ERROR.getCode(),
          ErrorMessage.QUERY_RESOURCE_ERROR.getMessage());
    }

    if (dataMap == null || dataMap.get("doc") == null) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_RESOURCE_ERROR.getCode(),
          ErrorMessage.QUERY_RESOURCE_ERROR.getMessage());
    }

    ResourceInfo resourceInfo = new ResourceInfo();
    resourceInfo.setContent((String) dataMap.get("doc"));
    resourceInfo.setKey((String) dataMap.get("key"));

    return resourceInfo;
  }
}
