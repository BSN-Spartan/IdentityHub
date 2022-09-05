// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.service;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.HubSecurityHandler;
import com.reddate.hub.hub.RequestUtils;
import com.reddate.hub.hub.config.HubConfig;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.constant.RequestUrlConstant;
import com.reddate.hub.hub.dto.HubResponse;
import com.reddate.hub.hub.dto.req.*;
import com.reddate.hub.hub.dto.resp.*;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.req.AddPermission;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.hub.param.req.UsedFlag;
import com.reddate.hub.hub.param.resp.AddPermissionResult;
import com.reddate.hub.hub.param.resp.DeletePermissionResult;
import com.reddate.hub.hub.param.resp.ResourceInfo;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.util.Secp256Util;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthService extends HubService {

  private ResourceService resourceService;
  
  private ConnService connService;

  public AuthService(HubConfig hubConfig) {
    super(hubConfig);
  }

  public AddPermissionResult createPermission(HubSecurityHandler handler, AddPermission permission)
      throws Exception {
    if (StringUtils.isEmpty(permission.getGrantUid())) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_USERID_EMPTY.getCode(), ErrorMessage.GRANT_USERID_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(permission.getGrantPublicKey())) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_USER_PUK_EMPTY.getCode(),
          ErrorMessage.GRANT_USER_PUK_EMPTY.getMessage());
    }

    if (permission.getGrant() == null) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_NOT_NULL.getCode(), ErrorMessage.GRANT_NOT_NULL.getMessage());
    }

    if (permission.getGrant() != Operation.WRITE
        && permission.getGrant() != Operation.UPDATE
        && permission.getGrant() != Operation.READ) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_ERROR.getCode(), ErrorMessage.GRANT_ERROR.getMessage());
    }

    if (permission.getGrant() == Operation.UPDATE
        || permission.getGrant() == Operation.READ
        || permission.getGrant() == Operation.DELETE) {
      if (permission.getUrl() == null || permission.getUrl().trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
      }
    }

    if (permission.getGrantUid().equals(handler.getUid())) {
      throw new IdentityHubException(
          ErrorMessage.NO_NEED_ADD_PERMISSION.getCode(),
          ErrorMessage.NO_NEED_ADD_PERMISSION.getMessage());
    }

    AddPermissionReq req = new AddPermissionReq();
    req.setGrantUid(permission.getGrantUid());
    req.setGrant(String.valueOf(permission.getGrant()));
    req.setUrl(permission.getUrl());
    String primaryKery = null;
    
    connService = new ConnService(hubConfig);
    Integer userCryptoType  = connService.getCryptoTypeByPublicKey(handler,null, handler.getPublicKey());
    Integer grantCryptoType  = connService.getCryptoTypeByPublicKey(handler, permission.getGrantUid(), null);
    if (permission.getGrant() == Operation.UPDATE
        || permission.getGrant() == Operation.READ
        || permission.getGrant() == Operation.DELETE) {
      ResourceInfo resourceInfo = null;
      try {
        resourceService = new ResourceService(hubConfig);
        resourceInfo = resourceService.getResource(handler, permission.getUrl());
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (Objects.isNull(resourceInfo)) {
        throw new IdentityHubException(
            ErrorMessage.RESOURCE_NOT_FOUND.getCode(),
            ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
      }
      if (StringUtils.isBlank(resourceInfo.getKey())) {
        throw new IdentityHubException(
            ErrorMessage.KEY_IS_EMPTY.getCode(), ErrorMessage.KEY_IS_EMPTY.getMessage());
      }
      
      if (permission.getGrantEncryptKey() == null
          || permission.getGrantEncryptKey().trim().isEmpty()) {
        try {
          primaryKery = handler.decrypt(resourceInfo.getKey());
          String encryptKey = Secp256Util.encrypt(CryptoType.ofVlaue(grantCryptoType), primaryKery, permission.getGrantPublicKey());
          req.setKey(encryptKey);
        } catch (Exception e) {
          throw new IdentityHubException(
              ErrorMessage.ENCRYPT_KEY_FAILED.getCode(),
              ErrorMessage.ENCRYPT_KEY_FAILED.getMessage());
        }
      } else {
        req.setKey(permission.getGrantEncryptKey());
      }
    } else {
      primaryKery = AesUtils.generalKey();
      req.setKey(Secp256Util.encrypt(CryptoType.ofVlaue(grantCryptoType), primaryKery, permission.getGrantPublicKey()));
      req.setKey1(Secp256Util.encrypt(CryptoType.ofVlaue(userCryptoType), primaryKery, handler.getPublicKey()));
    }

    HubResponse<AddPermissionResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig, handler, RequestUrlConstant.ADD_PERMISS_URL, req, AddPermissionResp.class);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.ADD_PERMISSION_FAILED.getCode(),
          ErrorMessage.ADD_PERMISSION_FAILED.getMessage());
    }

    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.ADD_PERMISSION_FAILED.getCode(),
          ErrorMessage.ADD_PERMISSION_FAILED.getMessage());
    }

    if (response.getCode() == ErrorMessage.SUCCESS.getCode()) {
      AddPermissionResp resp = response.getData();
      AddPermissionResult addPermissionResult = new AddPermissionResult();
      addPermissionResult.setKey(primaryKery);
      addPermissionResult.setUrl(resp.getUrl());
      return addPermissionResult;
    } else {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
  }

  public DeletePermissionResult deletePermission(
      HubSecurityHandler handler, String url, String granteeUid, Operation grant) throws Exception {
    if (grant == null) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_NOT_NULL.getCode(), ErrorMessage.GRANT_NOT_NULL.getMessage());
    }

    if (grant != Operation.WRITE
        && grant != Operation.UPDATE
        && grant != Operation.READ
        && grant != Operation.READ) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_ERROR.getCode(), ErrorMessage.GRANT_ERROR.getMessage());
    }

    if (StringUtils.isEmpty(granteeUid)) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_USERID_EMPTY.getCode(), ErrorMessage.GRANT_USERID_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(url)) {
      throw new IdentityHubException(
          ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
    }

    DeletePermissionReq req = new DeletePermissionReq();
    req.setGrantUid(granteeUid);
    req.setUrl(url);
    req.setGrant(grant.toString());

    HubResponse<DeletePermissionResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.DELTE_PERMISS_URL,
              req,
              DeletePermissionResp.class);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.DELETE_PERMISSION_FAILED.getCode(),
          ErrorMessage.DELETE_PERMISSION_FAILED.getMessage());
    }

    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.DELETE_PERMISSION_FAILED.getCode(),
          ErrorMessage.DELETE_PERMISSION_FAILED.getMessage());
    }

    if (response.getCode() == ErrorMessage.SUCCESS.getCode()) {
      DeletePermissionResult deletePermissionResult = new DeletePermissionResult();
      deletePermissionResult.setSucces(response.getData().isSucces());
      deletePermissionResult.setMessage(response.getData().getMessage());
      return deletePermissionResult;
    } else {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
  }

  public List<PermissionInfo> queryPermission(
      HubSecurityHandler handler, String grantUid, UsedFlag flag) throws Exception {

    if (flag != null && flag != UsedFlag.YES && flag != UsedFlag.NO) {
      throw new IdentityHubException(
          ErrorMessage.FLAG_ERROR.getCode(), ErrorMessage.FLAG_ERROR.getMessage());
    }

    QueryPermissionReq req = new QueryPermissionReq();
    if (flag == null) {
      req.setFlag("2");
    } else {
      req.setFlag(String.valueOf(flag.ordinal()));
    }

    if (grantUid != null && !grantUid.trim().isEmpty()) {
      req.setGrantUid(grantUid);
    }

    HubResponse<QueryPermissionResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.QUERY_PERMISS_URL,
              req,
              QueryPermissionResp.class);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_PERMISSION_FAILED.getCode(),
          ErrorMessage.QUERY_PERMISSION_FAILED.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_PERMISSION_FAILED.getCode(),
          ErrorMessage.QUERY_PERMISSION_FAILED.getMessage());
    }

    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }

    QueryPermissionResp queryPermissionResp = response.getData();

    List<PermissionInfo> list = new ArrayList<>();
    if (queryPermissionResp.getAuthList() != null && !queryPermissionResp.getAuthList().isEmpty()) {
      list.addAll(queryPermissionResp.getAuthList());
    }

    return list;
  }

  public List<GrantPermissionInfo> queryGrantedPermission(
      HubSecurityHandler handler, String ownerUid, Operation grant, UsedFlag flag)
      throws Exception {

    if (flag != null && flag != UsedFlag.YES && flag != UsedFlag.NO) {
      throw new IdentityHubException(
          ErrorMessage.FLAG_ERROR.getCode(), ErrorMessage.FLAG_ERROR.getMessage());
    }

    QueryGrantedPermissionReq req = new QueryGrantedPermissionReq();
    if (flag == null) {
      req.setFlag("2");
    } else {
      req.setFlag(String.valueOf(flag.ordinal()));
    }

    if (ownerUid != null && !ownerUid.trim().isEmpty()) {
      req.setOwnerUid(ownerUid);
    }

    if (grant != null) {
      req.setGrant(grant.name());
    }

    HubResponse<QueryGantPermissionResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.QUERY_GRANTED_PERMISS_URL,
              req,
              QueryGantPermissionResp.class);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_PERMISSION_FAILED.getCode(),
          ErrorMessage.QUERY_PERMISSION_FAILED.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.QUERY_PERMISSION_FAILED.getCode(),
          ErrorMessage.QUERY_PERMISSION_FAILED.getMessage());
    }

    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }

    QueryGantPermissionResp queryPermissionResp = response.getData();

    List<GrantPermissionInfo> list = new ArrayList<>();
    if (queryPermissionResp.getAuthList() != null && !queryPermissionResp.getAuthList().isEmpty()) {
      list.addAll(queryPermissionResp.getAuthList());
    }

    return list;
  }

  public CheckPermissionResp isPermission(
      HubSecurityHandler handler, String ownerUid, String grantUid, String url, Operation grant)
      throws Exception {
    if (StringUtils.isEmpty(ownerUid)) {
      throw new IdentityHubException(
          ErrorMessage.USERID_EMPTY.getCode(), ErrorMessage.USERID_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(grantUid)) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_USERID_EMPTY.getCode(), ErrorMessage.GRANT_USERID_EMPTY.getMessage());
    }

    if (grant == null) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_NOT_NULL.getCode(), ErrorMessage.GRANT_NOT_NULL.getMessage());
    }

    CheckPermissionReq req = new CheckPermissionReq();
    req.setGrantUid(grantUid);
    req.setOwnerUid(ownerUid);
    if (url != null && !url.trim().isEmpty()) {
      req.setUrl(url.trim());
    }
    req.setGrant(grant.toString());
    HubResponse<CheckPermissionResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig,
              handler,
              RequestUrlConstant.CHECK_PERMISS_URL,
              req,
              CheckPermissionResp.class);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.CHECK_PERMISSION_FAILED.getCode(),
          ErrorMessage.CHECK_PERMISSION_FAILED.getMessage());
    }

    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.CHECK_PERMISSION_FAILED.getCode(),
          ErrorMessage.CHECK_PERMISSION_FAILED.getMessage());
    }
    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
    return response.getData();
  }
}
