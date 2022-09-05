// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.service;

import java.util.ArrayList;
import java.util.List;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.HubSecurityHandler;
import com.reddate.hub.hub.RequestUtils;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.constant.RequestUrlConstant;
import com.reddate.hub.hub.dto.HubResponse;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.util.Secp256Util;
import org.apache.commons.lang3.StringUtils;

import com.reddate.hub.hub.config.HubConfig;
import com.reddate.hub.hub.dto.req.DeleteResourceReq;
import com.reddate.hub.hub.dto.req.QueryResourceHistoryReq;
import com.reddate.hub.hub.dto.req.QueryResourceReq;
import com.reddate.hub.hub.dto.req.SaveResourceReq;
import com.reddate.hub.hub.dto.req.TransferOwnerReq;
import com.reddate.hub.hub.dto.resp.CheckPermissionResp;
import com.reddate.hub.hub.dto.resp.DeleteResourceResp;
import com.reddate.hub.hub.dto.resp.QueryResourceHistoryResp;
import com.reddate.hub.hub.dto.resp.QueryResourceResp;
import com.reddate.hub.hub.dto.resp.ResourceHistoryInfo;
import com.reddate.hub.hub.dto.resp.SaveResourceResp;
import com.reddate.hub.hub.dto.resp.TransferOwnerResp;
import com.reddate.hub.hub.param.resp.ResourceInfo;
import com.reddate.hub.hub.param.resp.TransferOwnerResult;

public class ResourceService extends HubService {

  private ConnService connService;

  public ResourceService(HubConfig config) {
    super(config);
    connService = new ConnService(config);
  }

  public SaveResourceResp saveResource(
      HubSecurityHandler handler,
      String content,
      String url,
      String ownerUid,
      Operation grant,
      String resourceKey)
      throws Exception {
    if (StringUtils.isEmpty(content)) {
      throw new IdentityHubException(
          ErrorMessage.CONTNET_EMPTY.getCode(), ErrorMessage.CONTNET_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(ownerUid)) {
      throw new IdentityHubException(
          ErrorMessage.PUBLIC_KEY_EMPTY.getCode(), ErrorMessage.PUBLIC_KEY_EMPTY.getMessage());
    }

    if (grant == null) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_NOT_NULL.getCode(), ErrorMessage.GRANT_NOT_NULL.getMessage());
    }

    if (grant != Operation.WRITE && grant != Operation.UPDATE) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_ERROR.getCode(), ErrorMessage.GRANT_ERROR.getMessage());
    }

    if ((grant == Operation.UPDATE || grant == Operation.WRITE)
        && !handler.getUid().equals(ownerUid)) {
      if (StringUtils.isEmpty(url)) {
        throw new IdentityHubException(
            ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
      }
    }

    SaveResourceReq req = new SaveResourceReq();
    req.setUrl(url);
    req.setGrant(grant.toString());

    String enContent = content;
    String enKey = null;
    if (handler.getUid().equals(ownerUid)) {
      String key = AesUtils.generalKey();
      // enKey = Secp256Util.encrypt(this.getHubConfig().getCryptoType(), key, handler.getPublicKey());
      Integer cryptoType = connService.getCryptoTypeByPublicKey(handler, ownerUid,handler.getPublicKey());
      enKey = Secp256Util.encrypt(CryptoType.ofVlaue(cryptoType), key, handler.getPublicKey());
      enContent = AesUtils.encrypt(content, key);
      req.setEnContent(enContent);
      req.setEnKey(enKey);
      req.setOwnerUid(ownerUid);
    } else {
      String grantKey = null;
      String grantEnptyKey = null;
      if (resourceKey == null || resourceKey.isEmpty()) {
        AuthService authService = new AuthService(hubConfig);
        try {
          CheckPermissionResp resp =
              authService.isPermission(handler, ownerUid, handler.getUid(), url, grant);
          if (resp.isSucces()) {
            grantEnptyKey = resp.getKey();
          } else {
            throw new IdentityHubException(
                ErrorMessage.PERMISSION_USED_OR_NOT_HAVE.getCode(),
                ErrorMessage.PERMISSION_USED_OR_NOT_HAVE.getMessage());
          }
        } catch (IdentityHubException e) {
          throw e;
        } catch (Exception e) {
          e.printStackTrace();
          throw new IdentityHubException(
              ErrorMessage.CHECK_PERMISSION_FAILED.getCode(),
              ErrorMessage.CHECK_PERMISSION_FAILED.getMessage());
        }
        grantKey = handler.decrypt(grantEnptyKey);
        enKey = grantEnptyKey;
      } else {
        grantKey = resourceKey;
        Integer queryCryptoType = connService.getCryptoTypeByPublicKey(handler, handler.getUid(), null);
        enKey = Secp256Util.encrypt(CryptoType.ofVlaue(queryCryptoType), grantKey, handler.getPublicKey());
      }
      enContent = AesUtils.encrypt(content, grantKey);
      req.setEnContent(enContent);
      req.setEnKey(enKey);
      req.setOwnerUid(ownerUid);
    }

    HubResponse<SaveResourceResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.SAVE_RESOURCE_URL,
              req,
              SaveResourceResp.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.SAVE_RESOURCE_ERROR.getCode(),
          ErrorMessage.SAVE_RESOURCE_ERROR.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.SAVE_RESOURCE_ERROR.getCode(),
          ErrorMessage.SAVE_RESOURCE_ERROR.getMessage());
    }
    if (response.getCode() == ErrorMessage.SUCCESS.getCode()) {
      SaveResourceResp saveResourceResp = response.getData();
      saveResourceResp.setEncryptKey(enKey);
      return saveResourceResp;
    } else {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
  }

  public ResourceInfo getResource(HubSecurityHandler handler, String url) throws Exception {
    if (StringUtils.isEmpty(url)) {
      throw new IdentityHubException(
          ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
    }

    QueryResourceReq req = new QueryResourceReq();
    req.setUrl(url);
    HubResponse<QueryResourceResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.QUERY_RESOURCE_URL,
              req,
              QueryResourceResp.class);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_RESOURCE_ERROR.getCode(),
          ErrorMessage.QUERY_RESOURCE_ERROR.getMessage());
    }
    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
    QueryResourceResp queryResourceResp = response.getData();
    ResourceInfo resourceInfo = new ResourceInfo();
    resourceInfo.setContent(queryResourceResp.getContent());
    resourceInfo.setKey(queryResourceResp.getKey());
    return resourceInfo;
  }

  public Boolean deleteResource(HubSecurityHandler handler, String url) throws Exception {
    if (StringUtils.isEmpty(url)) {
      throw new IdentityHubException(
          ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
    }

    DeleteResourceReq req = new DeleteResourceReq();
    req.setUrl(url);
    HubResponse<DeleteResourceResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.DELTE_RESOURCE_URL,
              req,
              DeleteResourceResp.class);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.DELETE_RESOURCE_ERROR.getCode(),
          ErrorMessage.DELETE_RESOURCE_ERROR.getMessage());
    }
    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
    if (!response.getData().getSucces()) {
      return false;
    }
    return true;
  }

  public List<ResourceHistoryInfo> queryResourceHistory(
      HubSecurityHandler handler, String url, Operation grant) throws Exception {
    QueryResourceHistoryReq req = new QueryResourceHistoryReq();
    req.setUrl(url);
    if (grant != null) {
      req.setGrant(grant.name());
    }

    HubResponse<QueryResourceHistoryResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.QUERY_RESOURCE_HISTORY_URL,
              req,
              QueryResourceHistoryResp.class);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_RESOURCE_HISTORY_ERROR.getCode(),
          ErrorMessage.QUERY_RESOURCE_HISTORY_ERROR.getMessage());
    }
    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }

    List<ResourceHistoryInfo> historyList = new ArrayList<>();
    if (response.getData().getList() != null) {
      historyList.addAll(response.getData().getList());
    }

    return historyList;
  }

  public TransferOwnerResult transferOwner(
      HubSecurityHandler handler, String url, String newOwnerUid, String newKey) throws Exception {
    if (StringUtils.isEmpty(newOwnerUid)) {
      throw new IdentityHubException(
          ErrorMessage.NEW_OWNER_UID_EMPTY.getCode(),
          ErrorMessage.NEW_OWNER_UID_EMPTY.getMessage());
    }

    String newEnKey = newKey;
    String newOwnerPublicKey = connService.getPublicKey(handler, newOwnerUid);
    if (newOwnerPublicKey == null || newOwnerPublicKey.trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.OWNER_UID_NOT_EXISTS.getCode(),
          ErrorMessage.OWNER_UID_NOT_EXISTS.getMessage());
    }
    if (StringUtils.isBlank(newKey)) {
      ResourceInfo resourceInfo = this.getResource(handler, url);
      newEnKey =
          Secp256Util.encrypt(
              this.getHubConfig().getCryptoType(),
              handler.decrypt(resourceInfo.getKey()),
              newOwnerPublicKey);
    }

    TransferOwnerReq transferOwnerReq = new TransferOwnerReq();
    transferOwnerReq.setUrl(url);
    transferOwnerReq.setNewOwnerUid(newOwnerUid);
    transferOwnerReq.setNewKey(newEnKey);

    HubResponse<TransferOwnerResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.TRANSFER_OWNER_URL,
              transferOwnerReq,
              TransferOwnerResp.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.TRANSFER_OWNER_ERROR.getCode(),
          ErrorMessage.TRANSFER_OWNER_ERROR.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.TRANSFER_OWNER_ERROR.getCode(),
          ErrorMessage.TRANSFER_OWNER_ERROR.getMessage());
    }
    if (response.getCode() == ErrorMessage.SUCCESS.getCode()) {
      TransferOwnerResult transferOwnerResult = new TransferOwnerResult();
      transferOwnerResult.setSucces(true);
      return transferOwnerResult;
    } else {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
  }
}
