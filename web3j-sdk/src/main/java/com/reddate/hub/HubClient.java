// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub;

import java.util.List;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.DefaultHubSecurityHandler;
import com.reddate.hub.hub.HubConnection;
import com.reddate.hub.hub.HubSecurityHandler;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.req.AddPermission;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.hub.param.req.UsedFlag;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.util.Secp256Util;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.resp.CheckPermissionResp;
import com.reddate.hub.hub.dto.resp.GrantPermissionInfo;
import com.reddate.hub.hub.dto.resp.PermissionInfo;
import com.reddate.hub.hub.dto.resp.ResourceHistoryInfo;
import com.reddate.hub.hub.dto.resp.SaveResourceResp;
import com.reddate.hub.hub.param.resp.AddPermissionResult;
import com.reddate.hub.hub.param.resp.DeletePermissionResult;
import com.reddate.hub.hub.param.resp.RegisterHubResult;
import com.reddate.hub.hub.param.resp.ResourceInfo;
import com.reddate.hub.hub.param.resp.TransferOwnerResult;

public class HubClient {

  private HubConnection hubConnection;

  public HubClient(String hubURL, String hubPublicKey) {
    hubConnection = new HubConnection(CryptoType.ECDSA, hubURL, hubPublicKey);
  }

  public HubClient(CryptoType cryptoType, String hubURL, String hubPublicKey) {
    hubConnection = new HubConnection(cryptoType, hubURL, hubPublicKey);
  }

  public RegisterHubResult register(String uid, String privateKey, String publicKey,CryptoType cryptoType) {
    return hubConnection.register(uid, privateKey, publicKey, cryptoType);
  }

  public SaveResourceResp saveResource(
      String uid,
      String privateKey,
      String publicKey,
      String content,
      String url,
      String ownerUid,
      Operation grant,
      String key,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.saveResource(handler, content, url, ownerUid, grant, key);
  }

  public ResourceInfo getResource(
      String uid,
      String privateKey,
      String publicKey,
      String url,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.getResource(handler, url);
  }

  public Boolean deleteResource(
      String uid,
      String privateKey,
      String publicKey,
      String url,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.deleteResource(handler, url);
  }

  public AddPermissionResult createPermission(
      String uid,
      String privateKey,
      String publicKey,
      AddPermission permission,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.createPermission(handler, permission);
  }

  public DeletePermissionResult deletePermission(
      String uid,
      String privateKey,
      String publicKey,
      String url,
      String grantUid,
      Operation grant,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.deletePermission(handler, url, grantUid, grant);
  }

  public List<PermissionInfo> queryPermission(
      String uid,
      String privateKey,
      String publicKey,
      String grantUid,
      UsedFlag flag,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.queryPermission(handler, grantUid, flag);
  }

  public List<GrantPermissionInfo> queryGrantedPermission(
      String uid,
      String privateKey,
      String publicKey,
      String ownerUid,
      Operation grant,
      UsedFlag flag,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.queryGrantedPermission(handler, ownerUid, grant, flag);
  }

  public CheckPermissionResp isPermission(
      String uid,
      String privateKey,
      String publicKey,
      String ownerUid,
      String grantUid,
      String url,
      Operation grant,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.isPermission(handler, ownerUid, grantUid, url, grant);
  }

  public List<ResourceHistoryInfo> queryResourceHistory(
      String uid,
      String privateKey,
      String publicKey,
      String url,
      Operation grant,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.queryResourceHistory(handler, url, grant);
  }

  public TransferOwnerResult transferOwner(
      String uid,
      String privateKey,
      String publicKey,
      String url,
      String newOwnerUid,
      String newKey,
      String extraMsg,
      String extraMsgSign) {
    HubSecurityHandler handler =
        new DefaultHubSecurityHandler(hubConnection.getHubCryptoType(), uid, privateKey, publicKey);
    if (extraMsg != null
        && !extraMsg.trim().isEmpty()
        && extraMsgSign != null
        && !extraMsgSign.trim().isEmpty()) {
      handler.setExtraMsg(extraMsg, extraMsgSign);
    }
    return hubConnection.transferOwner(handler, url, newOwnerUid, newKey);
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

  public String getPublicKeyByUid(String uid) {
    return hubConnection.getPublicKeyByUid(uid);
  }
  
  public Integer getCryptoTypeByPublicKey(String uid,String publicKey) {
	  return hubConnection.getCryptoTypeByPublicKey(uid,publicKey);
  }
}
