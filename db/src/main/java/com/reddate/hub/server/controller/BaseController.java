// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.controller;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.HubData;
import com.reddate.hub.hub.dto.HubResponse;
import com.reddate.hub.hub.dto.req.RegisterHubReq;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.util.Secp256Util;
import com.reddate.hub.server.config.HubConfigInfo;
import com.reddate.hub.server.config.HubConfigUtils;
import com.reddate.hub.server.service.ConnService;
import com.reddate.hub.server.util.http.RequestParam;

public class BaseController {

  private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

  private static final String HUB_CONFIG_MAP_KEY = "hubConfig@" + BaseController.class.getName();

  private static final HashMap<String, HubConfigInfo> HUB_CONFIG_MAP = new HashMap<>();

  @Autowired protected ConnService connService;

  public <T extends HubData> RequestParam<T> parseRequestParam(Class<T> classes) {
    HttpServletRequest request = null;
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      request = attributes.getRequest();
    }
    if (request == null) {
      throw new IdentityHubException(
          ErrorMessage.HTTP_REQUEST_ERROR.getCode(), ErrorMessage.HTTP_REQUEST_ERROR.getMessage());
    }

    RequestParam<T> requestParam = null;
    try {
      requestParam = this.parseRequestParam(request, connService, classes);
    } catch (IdentityHubException e) {
      throw e;
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.PARSE_REQ_PARAM_FAILED.getCode(),
          ErrorMessage.PARSE_REQ_PARAM_FAILED.getMessage());
    }
    return requestParam;
  }

  private <T extends HubData> RequestParam<T> parseRequestParam(
      HttpServletRequest request, ConnService connService, Class<T> classes)
      throws SignatureException {
    HubConfigInfo hubConfigInfo = getHubConfigInfo();
    if (hubConfigInfo == null
        || hubConfigInfo.getPrivateKey() == null
        || hubConfigInfo.getPrivateKey().trim().isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.GET_HUB_PRIVATE_KEY_ERROR.getCode(),
          ErrorMessage.GET_HUB_PRIVATE_KEY_ERROR.getMessage());
    }

    String enptData = request.getParameter("data");
    if (enptData == null) {
      throw new IdentityHubException(
          ErrorMessage.MISSING_DADA.getCode(), ErrorMessage.MISSING_DADA.getMessage());
    }
    try {
      enptData =
          Secp256Util.decrypt(
              hubConfigInfo.getCryptoType(), enptData, hubConfigInfo.getPrivateKey().trim());
    } catch (Exception e1) {
      e1.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.REQUEST_PARAM_ERROR.getCode(),
          ErrorMessage.REQUEST_PARAM_ERROR.getMessage());
    }

    if (logger.isDebugEnabled()) {
      logger.debug(
          Thread.currentThread().getId()
              + " - Request ["
              + request.getRequestURI()
              + "] received client data: "
              + enptData);
    }

    Map<String, String> receivedData = (Map<String, String>) JSON.parseObject(enptData, Map.class);
    String receivedHash = receivedData.get("hash");
    enptData = receivedData.get("data");

    String uid = receivedData.get("uid");
    String clientPublicKey = receivedData.get("clientpublicKey");
    String extraMsg = receivedData.get("extraMsg");
    String did = null;
    String url = request.getRequestURI();
    String userPublicKey = null;
    if (url.endsWith("/conn/get") || url.endsWith("/conn/getType")) {
    	boolean verify = false;
    	try {
			verify = Secp256Util.verify(
			          hubConfigInfo.getCryptoType(), enptData, clientPublicKey, receivedHash);
		} catch (Exception e) {
			CryptoType retryCryptoType = null;
			if(hubConfigInfo.getCryptoType() == CryptoType.ECDSA) {
				retryCryptoType = CryptoType.ECDSA;
			}else {
				retryCryptoType = CryptoType.ECDSA;
			}
			verify = Secp256Util.verify(retryCryptoType, enptData, clientPublicKey, receivedHash);
		}
      if (!verify) {
        throw new IdentityHubException(
            ErrorMessage.VALIDATE_SIGN_ERROR.getCode(),
            ErrorMessage.VALIDATE_SIGN_ERROR.getMessage());
      }
    } else if (!url.endsWith("conn/conn")) {

      if (uid == null || uid.trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.USERID_EMPTY.getCode(), ErrorMessage.USERID_EMPTY.getMessage());
      }

      if (clientPublicKey == null || clientPublicKey.trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.PUBLIC_KEY_EMPTY.getCode(), ErrorMessage.PUBLIC_KEY_EMPTY.getMessage());
      }

      if (!Secp256Util.verify(
          hubConfigInfo.getCryptoType(), enptData, clientPublicKey, receivedHash)) {
        throw new IdentityHubException(
            ErrorMessage.VALIDATE_SIGN_ERROR.getCode(),
            ErrorMessage.VALIDATE_SIGN_ERROR.getMessage());
      }

      userPublicKey = connService.queryPublicKey(uid);
      if (userPublicKey == null || userPublicKey.trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.USER_NOT_EXISTS.getCode(), ErrorMessage.USER_NOT_EXISTS.getMessage());
      }

      if (!userPublicKey.equals(clientPublicKey)) {
        if (extraMsg == null || extraMsg.trim().isEmpty()) {
          throw new IdentityHubException(
              ErrorMessage.VALIDATE_SIGN_ERROR.getCode(),
              ErrorMessage.VALIDATE_SIGN_ERROR.getMessage());
        }
        if (logger.isDebugEnabled()) {
          logger.debug("extraMsg:" + extraMsg);
        }
        String str01 = extraMsg.substring(2, extraMsg.lastIndexOf("\",\""));
        String str02 = extraMsg.substring(extraMsg.lastIndexOf("\",\"") + 3, extraMsg.length() - 2);
        if (logger.isDebugEnabled()) {
          logger.debug("orgin mesage:" + str01);
          logger.debug("sign string:" + str02);
        }

        boolean verifyExtraMsg = false;
        
        try {
			verifyExtraMsg = Secp256Util.verify(hubConfigInfo.getCryptoType(), str01, userPublicKey, str02);
		} catch (Exception e) {
			CryptoType retryCryptoType = null;
			if(hubConfigInfo.getCryptoType() == CryptoType.ECDSA) {
				retryCryptoType = CryptoType.ECDSA;
			}else {
				retryCryptoType = CryptoType.ECDSA;
			}
			verifyExtraMsg = Secp256Util.verify(retryCryptoType, str01, userPublicKey, str02);
		}
        
        if (!verifyExtraMsg) {
          throw new IdentityHubException(
              ErrorMessage.VALIDATE_SIGN_ERROR.getCode(),
              ErrorMessage.VALIDATE_SIGN_ERROR.getMessage());
        }
      }

    } else {

      if (!Secp256Util.verify(
          hubConfigInfo.getCryptoType(), enptData, clientPublicKey, receivedHash)) {
        throw new IdentityHubException(
            ErrorMessage.VALIDATE_SIGN_ERROR.getCode(),
            ErrorMessage.VALIDATE_SIGN_ERROR.getMessage());
      }

      RegisterHubReq requestData = this.paseDataFromStr(enptData, RegisterHubReq.class);
      userPublicKey = requestData.getPublicKey();
    }

    T requestData = this.paseDataFromStr(enptData, classes);

    return new RequestParam<>(uid, userPublicKey, requestData, clientPublicKey);
  }

  private <T extends HubData> T paseDataFromStr(String dataStr, Class<T> classes) {
    T obj = null;
    try {
      obj = (T) classes.getConstructor().newInstance();
      obj = (T) obj.paseFromSeriString(dataStr);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.REQUEST_PARAM_ERROR.getCode(),
          ErrorMessage.REQUEST_PARAM_ERROR.getMessage());
    }
    return obj;
  }

  private String getPublicKey() throws Exception {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    HubConfigInfo hubConfigInfo = getHubConfigInfo();
    String enptData = request.getParameter("data");
    enptData =
        Secp256Util.decrypt(
            hubConfigInfo.getCryptoType(), enptData, hubConfigInfo.getPrivateKey().trim());
    Map<String, String> receivedData = (Map<String, String>) JSON.parseObject(enptData, Map.class);
    enptData = receivedData.get("data");
    String userId = receivedData.get("uid");
    String userPublicKey = connService.queryPublicKey(userId);
    if (userPublicKey == null || userPublicKey.trim().isEmpty()) {
      userPublicKey = receivedData.get("clientpublicKey");
    }
    return userPublicKey;
  }

  public <T extends HubData> ResponseEntity<String> success(String publicKey, T data) {
    HubConfigInfo hubConfigInfo = getHubConfigInfo();

    String dataStr = null;
    if (data != null) {
      dataStr = data.seriToString();
    }
    if (logger.isDebugEnabled()) {
      logger.debug(
          Thread.currentThread().getId() + " - process success ,send to client : " + dataStr);
    }

    HubResponse<T> response = new HubResponse<>();
    response.setCode(0);
    response.setMsg("Success");
    response.setData(data);
    String encptData = response.formatToString();

    try {
      encptData = Secp256Util.encrypt(hubConfigInfo.getCryptoType(), encptData, publicKey);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.CODE_ERROR.getCode(), ErrorMessage.CODE_ERROR.getMessage());
    }

    return ResponseEntity.ok()
        .header("Access-Control-Allow-Origin", "*")
        .contentType(MediaType.APPLICATION_JSON)
        .body(encptData);
  }

  public <T extends HubData> ResponseEntity<String> error(
      Integer code, String message, String publicKey) {
    HubConfigInfo hubConfigInfo = getHubConfigInfo();

    HubResponse<T> response = new HubResponse<>();
    response.setCode(code);
    response.setMsg(message);

    if (publicKey == null || publicKey.trim().isEmpty()) {
      try {
        publicKey = getPublicKey();
      } catch (Exception e) {
        logger.info("Unknow Client connect");
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug(
          Thread.currentThread().getId()
              + " - process failed ,send to client : [cdoe = 400 , msg = "
              + message
              + "]");
    }

    String encptData = response.formatToString();

    try {
      encptData = Secp256Util.encrypt(hubConfigInfo.getCryptoType(), encptData, publicKey);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IdentityHubException(
          ErrorMessage.CODE_ERROR.getCode(), ErrorMessage.CODE_ERROR.getMessage());
    }

    return ResponseEntity.ok()
        .header("Access-Control-Allow-Origin", "*")
        .contentType(MediaType.APPLICATION_JSON)
        .body(encptData);
  }

  private HubConfigInfo getHubConfigInfo() {
    HubConfigInfo hubConfigInfo = HUB_CONFIG_MAP.get(HUB_CONFIG_MAP_KEY);
    if (hubConfigInfo != null) {
      return hubConfigInfo;
    }
    hubConfigInfo = HubConfigUtils.getHubConfig();
    HUB_CONFIG_MAP.put(HUB_CONFIG_MAP_KEY, hubConfigInfo);
    logger.info("put hub config info to class map, map key is: " + HUB_CONFIG_MAP_KEY);
    return hubConfigInfo;
  }
}
