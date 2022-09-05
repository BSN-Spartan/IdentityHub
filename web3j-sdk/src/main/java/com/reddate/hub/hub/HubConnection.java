// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.param.req.AddPermission;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.hub.param.req.UsedFlag;
import com.reddate.hub.pojo.KeyPair;
import com.reddate.hub.util.Secp256Util;
import org.apache.commons.lang3.StringUtils;

import com.reddate.hub.hub.config.HubConfig;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.resp.CheckPermissionResp;
import com.reddate.hub.hub.dto.resp.GrantPermissionInfo;
import com.reddate.hub.hub.dto.resp.PermissionInfo;
import com.reddate.hub.hub.dto.resp.RegisterHubResp;
import com.reddate.hub.hub.dto.resp.ResourceHistoryInfo;
import com.reddate.hub.hub.dto.resp.SaveResourceResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.resp.AddPermissionResult;
import com.reddate.hub.hub.param.resp.DeletePermissionResult;
import com.reddate.hub.hub.param.resp.RegisterHubResult;
import com.reddate.hub.hub.param.resp.ResourceInfo;
import com.reddate.hub.hub.param.resp.TransferOwnerResult;
import com.reddate.hub.hub.service.AuthService;
import com.reddate.hub.hub.service.ConnService;
import com.reddate.hub.hub.service.ResourceService;

import java.math.BigInteger;
import java.util.List;

public class HubConnection {

  private HubConfig hubConfig;

  private ConnService connService;

  private ResourceService resourceService;

  private AuthService authService;

  public HubConnection(CryptoType cryptoType, String hubUrl, String hubPublicKey) {
    this.hubConfig = HubConfig.parseConfigInfo(cryptoType, hubUrl, hubPublicKey);
    this.authService = new AuthService(hubConfig);
    this.connService = new ConnService(hubConfig);
    this.resourceService = new ResourceService(hubConfig);
  }

  public RegisterHubResult register(String uid, String privateKey, String publicKey,CryptoType cryptoType) {
    if (publicKey == null || publicKey.trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.PUBLIC_KEY_EMPTY.getCode(), ErrorMessage.PUBLIC_KEY_EMPTY.getMessage());
    }
    
    if (!Secp256Util.isValidedPublickKey(cryptoType, publicKey)) {
      throw new IdentityHubException(
          ErrorMessage.PUBLIC_KEY_FORMAT_ERROR.getCode(),
          ErrorMessage.PUBLIC_KEY_FORMAT_ERROR.getMessage());
    }

    String regiterUid = uid;
    if (uid == null || uid.trim().isEmpty()) {
      try {
        regiterUid = Secp256Util.getAddress(cryptoType, new BigInteger(publicKey));
      } catch (Exception e1) {
        throw new IdentityHubException(
            ErrorMessage.GEN_USER_ID_FAILED.getCode(),
            ErrorMessage.GEN_USER_ID_FAILED.getMessage());
      }
    } else {
      regiterUid = regiterUid.trim();
    }

    HubSecurityHandler handler = null;
    if (publicKey == null || publicKey.trim().isEmpty()) {
      handler = new DefaultHubSecurityHandler(this.getHubCryptoType(), regiterUid, privateKey);
    } else {
      handler =
          new DefaultHubSecurityHandler(this.getHubCryptoType(), regiterUid, privateKey, publicKey);
    }
    handler.setExtraMsg(publicKey, publicKey);
    try {
      RegisterHubResp regHubResp = connService.connHub(handler,publicKey,cryptoType);
      RegisterHubResult result = new RegisterHubResult();
      result.setSuccess(regHubResp.isSuccess());
      result.setMessage(regHubResp.getMessage());
      if (regHubResp.isSuccess()) {
        result.setUid(regiterUid);
      }
      return result;
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
  }

  public SaveResourceResp saveResource(
      HubSecurityHandler handler,
      String content,
      String url,
      String ownerUid,
      Operation grant,
      String key) {
    SaveResourceResp saveResourceResp = null;
    try {
      saveResourceResp = resourceService.saveResource(handler, content, url, ownerUid, grant, key);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return saveResourceResp;
  }

  public ResourceInfo getResource(HubSecurityHandler handler, String url) {
    ResourceInfo saveResourceResp = null;
    try {
      saveResourceResp = resourceService.getResource(handler, url);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return saveResourceResp;
  }

  public Boolean deleteResource(HubSecurityHandler handler, String url) {
    if (StringUtils.isEmpty(url)) {
      throw new IdentityHubException(
          ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
    }

    Boolean deleteResult = false;
    try {
      deleteResult = resourceService.deleteResource(handler, url);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return deleteResult;
  }

  public AddPermissionResult createPermission(
      HubSecurityHandler handler, AddPermission permission) {
    AddPermissionResult addPermissionResult = null;
    try {
      addPermissionResult = authService.createPermission(handler, permission);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return addPermissionResult;
  }

  public DeletePermissionResult deletePermission(
      HubSecurityHandler handler, String url, String grantUid, Operation grant) {
    DeletePermissionResult deletePermissionResult = null;
    try {
      deletePermissionResult = authService.deletePermission(handler, url, grantUid, grant);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      deletePermissionResult = new DeletePermissionResult();
      deletePermissionResult.setSucces(false);
      deletePermissionResult.setMessage(e.getMessage());
    }
    return deletePermissionResult;
  }

  public List<PermissionInfo> queryPermission(
      HubSecurityHandler handler, String grantUid, UsedFlag flag) {
    List<PermissionInfo> list = null;
    try {
      list = authService.queryPermission(handler, grantUid, flag);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return list;
  }

  public List<GrantPermissionInfo> queryGrantedPermission(
      HubSecurityHandler handler, String ownerUid, Operation grant, UsedFlag flag) {
    List<GrantPermissionInfo> list = null;
    try {
      list = authService.queryGrantedPermission(handler, ownerUid, grant, flag);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return list;
  }

  public CheckPermissionResp isPermission(
      HubSecurityHandler handler, String ownerUid, String granteeUid, String url, Operation grant) {
    CheckPermissionResp resp = null;
    try {
      resp = authService.isPermission(handler, ownerUid, granteeUid, url, grant);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return resp;
  }

  public List<ResourceHistoryInfo> queryResourceHistory(
      HubSecurityHandler handler, String url, Operation grant) {
    List<ResourceHistoryInfo> list = null;
    try {
      list = resourceService.queryResourceHistory(handler, url, grant);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
    return list;
  }

  public CryptoType getHubCryptoType() {
    return hubConfig.getCryptoType();
  }

  public String getPublicKeyByUid(String uid) {
    if (uid == null || uid.trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.USERID_EMPTY.getCode(), ErrorMessage.USERID_EMPTY.getMessage());
    }

    try {
      KeyPair keyPair = Secp256Util.createKeyPair(getHubCryptoType());
      HubSecurityHandler handler =
          new DefaultHubSecurityHandler(
              getHubCryptoType(), uid, keyPair.getPrivateKey(), keyPair.getPublicKey());
      return connService.getPublicKey(handler, uid);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
  }

  public TransferOwnerResult transferOwner(
      HubSecurityHandler handler, String url, String newOwnerUid, String newKey) {
    try {
      return resourceService.transferOwner(handler, url, newOwnerUid, newKey);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
  }
  
 public Integer getCryptoTypeByPublicKey(String uid,String publicKey) {
	 if(uid == null || uid.trim().isEmpty()) {
		 if (publicKey == null || publicKey.trim().isEmpty()) {
		        throw new IdentityHubException(
		            ErrorMessage.PUBLIC_KEY_EMPTY.getCode(), ErrorMessage.PUBLIC_KEY_EMPTY.getMessage());
		      }
	 } 

	      try {
	        KeyPair keyPair = Secp256Util.createKeyPair(getHubCryptoType());
	        HubSecurityHandler handler =
	            new DefaultHubSecurityHandler(
	                getHubCryptoType(), null, keyPair.getPrivateKey(), keyPair.getPublicKey());
	        return connService.getCryptoTypeByPublicKey(handler,uid, publicKey);
	      } catch (IdentityHubException e) {
	        throw e;
	      } catch (Exception e) {
	        throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
	      }
  }
  
}
