// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service;

import java.time.LocalDateTime;
import java.util.List;

import com.reddate.hub.server.dto.HubMongoRecord;
import com.reddate.hub.server.dto.SyncDataReq;
import com.reddate.hub.server.dto.SyncDataResp;

public interface SynchronizeDataService {

  public List<HubMongoRecord> querySynchronizeData(LocalDateTime lastSyncTime, LocalDateTime now);

  public SyncDataResp processSyncData(SyncDataReq vo, String aesKey);
}
