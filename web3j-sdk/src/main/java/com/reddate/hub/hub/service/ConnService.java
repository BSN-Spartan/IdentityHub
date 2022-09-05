// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.service;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.HubSecurityHandler;
import com.reddate.hub.hub.RequestUtils;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.constant.RequestUrlConstant;
import com.reddate.hub.hub.dto.HubResponse;
import com.reddate.hub.hub.dto.req.QueryCryptoTypPublicKeyReq;
import com.reddate.hub.hub.dto.req.QueryRegPublicKeyReq;
import com.reddate.hub.hub.dto.req.RegisterHubReq;
import com.reddate.hub.hub.dto.resp.GetUidInfoResp;
import com.reddate.hub.hub.dto.resp.QueryCryptoTypPublicKeyResp;
import com.reddate.hub.hub.dto.resp.RegisterHubResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.config.HubConfig;

public class ConnService extends HubService {

  public ConnService(HubConfig config) {
    super(config);
  }

  public RegisterHubResp connHub(HubSecurityHandler handler, String publicKey, CryptoType cryptoType) {
    RegisterHubReq req = new RegisterHubReq();
    req.setUid(handler.getUid());
    req.setPublicKey(handler.getPublicKey());
    req.setCryptoType(cryptoType.ordinal());
    try {
      HubResponse<RegisterHubResp> response =
          RequestUtils.sendPostRequest(
              hubConfig, handler, RequestUrlConstant.CONN_URL, req, RegisterHubResp.class);
      if (response.getCode() == ErrorMessage.SUCCESS.getCode()) {
        RegisterHubResp hubResp = response.getData();
        return hubResp;
      } else {
        throw new IdentityHubException(response.getCode(), response.getMsg());
      }
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage());
    }
  }

  public String getPublicKey(HubSecurityHandler handler, String uid) {
    QueryRegPublicKeyReq req = new QueryRegPublicKeyReq();
    req.setRegUserId(uid);
    HubResponse<GetUidInfoResp> response = null;
    try {
      response =
          RequestUtils.sendPostRequest(
              hubConfig, handler, RequestUrlConstant.GET_DID_INFO, req, GetUidInfoResp.class);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.GET_PUBLIC_KEY_FAILED.getCode(),
          ErrorMessage.GET_PUBLIC_KEY_FAILED.getMessage() + " " + e.getMessage());
    }
    if (response == null) {
      throw new IdentityHubException(
          ErrorMessage.GET_PUBLIC_KEY_FAILED.getCode(),
          ErrorMessage.GET_PUBLIC_KEY_FAILED.getMessage());
    }
    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
      throw new IdentityHubException(response.getCode(), response.getMsg());
    }
    return response.getData().getPublicKey();
  }

  public Integer getCryptoTypeByPublicKey(HubSecurityHandler handler,String userId, String publicKey) {
	  	QueryCryptoTypPublicKeyReq req = new QueryCryptoTypPublicKeyReq();
	  	req.setRegUserId(userId);
	  	req.setPublicKey(publicKey);
	    HubResponse<QueryCryptoTypPublicKeyResp> response = null;
	    try {
	      response =
	          RequestUtils.sendPostRequest(
	              hubConfig, handler, RequestUrlConstant.GET_TYPE_INFO, req, QueryCryptoTypPublicKeyResp.class);
	    } catch (Exception e) {
	      throw new IdentityHubException(
	          ErrorMessage.GET_CRYPTO_TYPE_FAILED.getCode(),
	          ErrorMessage.GET_CRYPTO_TYPE_FAILED.getMessage() + " " + e.getMessage());
	    }
	    if (response == null) {
	      throw new IdentityHubException(
	          ErrorMessage.GET_CRYPTO_TYPE_FAILED.getCode(),
	          ErrorMessage.GET_CRYPTO_TYPE_FAILED.getMessage());
	    }
	    if (response.getCode() != ErrorMessage.SUCCESS.getCode()) {
	      throw new IdentityHubException(response.getCode(), response.getMsg());
	    }
	    return response.getData().getType();
	  }

}
