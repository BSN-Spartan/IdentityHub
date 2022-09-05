// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.util;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.server.dto.HubMongoRecord;
import com.reddate.hub.server.dto.SyncDataResp;
import com.reddate.hub.server.util.http.SendSynchronizeDataUtils;

public class SynchronizeDataThread implements Callable<SyncDataResp> {

  private static final Logger logger = LoggerFactory.getLogger(SynchronizeDataThread.class);

  private String url;

  private String hubId;

  private String aesKey;

  private List<HubMongoRecord> dataList;

  public SynchronizeDataThread(
      String url, String hubId, String aesKey, List<HubMongoRecord> dataList) {
    super();
    this.url = url;
    this.hubId = hubId;
    this.aesKey = aesKey;
    this.dataList = dataList;
  }

  @Override
  public SyncDataResp call() throws Exception {
    try {
      SyncDataResp syncDataResp =
          SendSynchronizeDataUtils.sendPostRequest(url, hubId, aesKey, dataList);
      syncDataResp.setHubId(hubId);
      return syncDataResp;
    } catch (Exception e) {
      e.printStackTrace();
    }
    SyncDataResp resp = new SyncDataResp();
    resp.setCode(ErrorMessage.UNKNOWN_ERROR.getCode());
    resp.setHubId(hubId);
    resp.setMsg("Connection timed out");
    return resp;
  }
}
