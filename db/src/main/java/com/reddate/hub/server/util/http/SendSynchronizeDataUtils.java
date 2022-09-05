// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.server.dto.HubMongoRecord;
import com.reddate.hub.server.dto.SyncDataResp;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendSynchronizeDataUtils {

  private static final Logger logger = LoggerFactory.getLogger(SendSynchronizeDataUtils.class);

  private static MediaType MEDIA_JSON_TYPE = MediaType.parse("application/json;charset=utf-8");

  public static SyncDataResp sendPostRequest(
      String url, String hubId, String aesKey, List<HubMongoRecord> dataList) throws Exception {
    String sendData = JSONObject.toJSONString(dataList);
    if (logger.isDebugEnabled()) {
      logger.debug("send data to other hub (" + url + "): " + sendData);
    }

    sendData = AesUtils.encrypt(sendData, aesKey);

    Map<String, String> sendDataMap = new HashMap<>();
    sendDataMap.put("id", hubId);
    sendDataMap.put("data", sendData);

    RequestBody requestBody =
        RequestBody.create(JSONObject.toJSONString(sendDataMap), MEDIA_JSON_TYPE);
    Request request = new Request.Builder().url(url).post(requestBody).build();

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

    if (logger.isDebugEnabled()) {
      logger.debug("send data to other hub result (" + url + "): " + resposneData);
    }

    SyncDataResp syncDataResp = JSON.parseObject(resposneData, SyncDataResp.class);

    return syncDataResp;
  }
}
