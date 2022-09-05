// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.QueryCryptoTypPublicKeyReq;
import com.reddate.hub.hub.dto.req.QueryRegPublicKeyReq;
import com.reddate.hub.hub.dto.req.RegisterHubReq;
import com.reddate.hub.hub.dto.resp.QueryCryptoTypPublicKeyResp;
import com.reddate.hub.hub.dto.resp.QueryRegPublicKeyResp;
import com.reddate.hub.hub.dto.resp.RegisterHubResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.server.service.ConnService;
import com.reddate.hub.server.util.http.RequestParam;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conn")
public class ConnHubController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(ConnHubController.class);

  @Autowired private ConnService connService;

  @PostMapping("/conn")
  public ResponseEntity<String> connHub() {
    RequestParam<RegisterHubReq> requestParam = null;
    try {
      requestParam = parseRequestParam(RegisterHubReq.class);
      // logger.info("connection information is:" + JSONObject.toJSONString(requestParam));
      RegisterHubResp connHubResp =
          connService.registerHub(requestParam.getUid(), requestParam.getData());
      return success(requestParam.getClientPublicKey(), connHubResp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.REGISTER_PUBLIC_KEY_ERROR.getCode(),
          ErrorMessage.REGISTER_PUBLIC_KEY_ERROR.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/get")
  public ResponseEntity<String> getPublicKey() {
    RequestParam<QueryRegPublicKeyReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryRegPublicKeyReq.class);
      String publicKey = connService.queryPublicKey(requestParam.getData().getRegUserId());
      QueryRegPublicKeyResp resp = new QueryRegPublicKeyResp();
      resp.setPublicKey(publicKey);
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.GET_PUBLIC_KEY_FAILED.getCode(),
          ErrorMessage.GET_PUBLIC_KEY_FAILED.getMessage(),
          requestParam.getClientPublicKey());
    }
  }
  
  @PostMapping("/getType")
  public ResponseEntity<String> getCryptoTypeByPublicKey() {
    RequestParam<QueryCryptoTypPublicKeyReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryCryptoTypPublicKeyReq.class);
      Integer cryptoType = connService.getCryptoTypeByPublicKey(requestParam.getData().getRegUserId(), requestParam.getData().getPublicKey());
      QueryCryptoTypPublicKeyResp resp = new QueryCryptoTypPublicKeyResp();
      resp.setType(cryptoType);
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.GET_CRYPTO_TYPE_FAILED.getCode(),
          ErrorMessage.GET_CRYPTO_TYPE_FAILED.getMessage(),
          requestParam.getClientPublicKey());
    }
  }
  
  
}
