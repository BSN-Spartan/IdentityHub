// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.reddate.hub.hub.dto.HubData;
import com.reddate.hub.hub.dto.HubResponse;
import com.reddate.hub.pojo.KeyPair;
import com.reddate.hub.util.Secp256Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.hub.config.HubConfig;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestUtils {

  private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

  public static <T extends HubData> HubResponse<T> sendPostRequest(
      HubConfig hubConfig,
      HubSecurityHandler handler,
      String url,
      HubData param,
      Class<T> returnType)
      throws Exception {
    String hubUrl = hubConfig.getHubUrl();
    String enptKey = hubConfig.getHubPublicKey();

    String extraMessage = handler.getExtraMsg();
    KeyPair keyPair = null;
    if (extraMessage != null && !extraMessage.trim().isEmpty()) {
      keyPair = Secp256Util.createKeyPair(handler.getCryptoType());
    }

    String sendData = param.seriToString();
    String hash = null;
    if (keyPair == null) {
      hash = handler.sign(sendData);
    } else {
      hash = Secp256Util.sign(handler.getCryptoType(), sendData, keyPair.getPrivateKey());
    }

    // String hash = handler.sign(sendData);
    Map<String, String> sendDataMap = new HashMap<>();
    sendDataMap.put("data", sendData);
    sendDataMap.put("uid", handler.getUid());
    sendDataMap.put("hash", hash);
    if (keyPair == null) {
      sendDataMap.put("clientpublicKey", handler.getPublicKey());
    } else {
      sendDataMap.put("clientpublicKey", keyPair.getPublicKey());
      sendDataMap.put("extraMsg", handler.getExtraMsg());
    }

    if (logger.isDebugEnabled()) {
      logger.debug("send data to hub (" + hubUrl + url + ") " + sendDataMap);
    }
    sendData = JSONObject.toJSONString(sendDataMap);
    sendData = Secp256Util.encrypt(handler.getCryptoType(), sendData, enptKey);

    RequestBody formBody = new FormBody.Builder().add("data", sendData).build();

    Request request = new Request.Builder().url(hubUrl + url).post(formBody).build();

    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
    Response response = null;
    try {
      response = client.newCall(request).execute();
    } catch (IOException e) {
      response.close();
      throw new IdentityHubException(
          ErrorMessage.SEND_REQ_ERROR.getCode(), ErrorMessage.SEND_REQ_ERROR.getMessage());
    }

    int httpResonseCode = response.code();
    if (httpResonseCode != 200) {
      response.close();
      throw new IdentityHubException(
          ErrorMessage.SEND_REQ_ERROR.getCode(), ErrorMessage.SEND_REQ_ERROR.getMessage());
    }

    String resposneData = response.body().string();
    response.close();
    if (keyPair == null) {
      resposneData = handler.decrypt(resposneData);
    } else {
      resposneData =
          Secp256Util.decrypt(handler.getCryptoType(), resposneData, keyPair.getPrivateKey());
    }

    HubResponse<T> returnTmp = HubResponse.parseFormString(resposneData, returnType);
    return returnTmp;
  }
}
