// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.server.dto.HubMongoRecord;
import com.reddate.hub.server.dto.SyncDataResp;
import com.reddate.hub.server.service.SynchronizeDataService;
import com.reddate.hub.server.service.impl.ConnServiceImpl;
import com.reddate.hub.server.util.FileUtils;
import com.reddate.hub.server.util.SynchronizeDataThread;
import com.reddate.hub.server.util.http.SendSynchronizeDataUtils;

@Configuration
@EnableScheduling
public class SyncHubData {

  private static final Logger logger = LoggerFactory.getLogger(SyncHubData.class);

  @Autowired private SynchronizeDataService synchronizeDataService;

  @Autowired private SynchronizeDataConfig syncDataConfig;

  private Boolean checkConfig = true;

  private static final ExecutorService executorService = Executors.newCachedThreadPool();

  @Scheduled(cron = "0/30 * * * * ?") // 30秒执行一次
  private void configureTasks() {
    checkSyncHubConfig();

    if (syncDataConfig.getEnable()) {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime lastSyncTime = LocalDateTime.parse("2021-01-01T00:00:00");
      String lastTimeInFile = null;
      try {
        lastTimeInFile = FileUtils.readContent(syncDataConfig.getLastSyncFileName());
      } catch (Exception e2) {
        logger.debug("Can not find last time file");
      }

      if (lastTimeInFile != null && !lastTimeInFile.trim().isEmpty()) {
        lastSyncTime = LocalDateTime.parse(lastTimeInFile);
      }
      logger.debug(
          "begin to query need synchronize data, the synchronize dead line is {}",
          lastSyncTime.toString());

      List<HubMongoRecord> dataList =
          synchronizeDataService.querySynchronizeData(lastSyncTime, now);

      if (dataList.isEmpty()) {
        logger.info(
            "No data need synchronize to other Hub, the synchronize dead line is {}",
            lastSyncTime.toString());
      } else {
        List<Future<SyncDataResp>> resultList = new ArrayList<>();
        for (int i = 0; i < syncDataConfig.getHost().size(); i++) {
          HostInfo hostInfo = syncDataConfig.getHost().get(i);
          Future<SyncDataResp> resultFuture =
              executorService.submit(
                  new SynchronizeDataThread(
                      hostInfo.getUrl(), hostInfo.getId(), hostInfo.getAesKey(), dataList));
          resultList.add(resultFuture);
        }

        int finishCnt = 0;
        do {
          for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).isDone()) {
              finishCnt++;
            } else {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        } while (finishCnt < resultList.size());

        boolean hasFailed = false;
        for (int i = 0; i < resultList.size(); i++) {
          try {
            SyncDataResp syncDataResp = resultList.get(i).get();
            if (syncDataResp.getCode() != 0) {
              logger.error(
                  "Send synchronize data to other hub failed, hub Id is {} , error messge is {}",
                  syncDataResp.getHubId(),
                  syncDataResp.getMsg());
              hasFailed = true;
            }
          } catch (InterruptedException e1) {
            e1.printStackTrace();
            hasFailed = true;
          } catch (ExecutionException e1) {
            e1.printStackTrace();
            hasFailed = true;
          }
        }

        if (!hasFailed) {
          FileUtils.writeContent2File(now.toString(), syncDataConfig.getLastSyncFileName());
          logger.info(
              "Total synchronize to other Hub is {} , the synchronize dead line is {}",
              dataList.size(),
              lastSyncTime.toString());
        }
      }
    }
  }

  private void checkSyncHubConfig() {
    if (syncDataConfig == null || syncDataConfig.getEnable() == null) {
      throw new IdentityHubException(
          ErrorMessage.SYNC_HUB_DATA_ENABLE_EMPTY.getCode(),
          ErrorMessage.SYNC_HUB_DATA_ENABLE_EMPTY.getMessage());
    }
    if (syncDataConfig.getEnable()) {
      if (syncDataConfig.getLastSyncFileName() == null
          || syncDataConfig.getLastSyncFileName().trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.SYNC_HUB_LAST_FILE_PATH_EMPTY.getCode(),
            ErrorMessage.SYNC_HUB_LAST_FILE_PATH_EMPTY.getMessage());
      }

      if (syncDataConfig.getHost().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.SYNC_HUB_DATA_HOST_EMPTY.getCode(),
            ErrorMessage.SYNC_HUB_DATA_HOST_EMPTY.getMessage());
      }
      for (HostInfo hostInfo : syncDataConfig.getHost()) {
        if (hostInfo.getId() == null || hostInfo.getId().trim().isEmpty()) {
          throw new IdentityHubException(
              ErrorMessage.SYNC_HUB_DATA_HOST_ID_EMPTY.getCode(),
              ErrorMessage.SYNC_HUB_DATA_HOST_ID_EMPTY.getMessage());
        }
        if (hostInfo.getUrl() == null || hostInfo.getUrl().trim().isEmpty()) {
          throw new IdentityHubException(
              ErrorMessage.SYNC_HUB_DATA_HOST_URL_EMPTY.getCode(),
              ErrorMessage.SYNC_HUB_DATA_HOST_URL_EMPTY.getMessage());
        }
        if (hostInfo.getAesKey() == null || hostInfo.getAesKey().trim().isEmpty()) {
          throw new IdentityHubException(
              ErrorMessage.SYNC_HUB_DATA_HOST_AES_KEY_EMPTY.getCode(),
              ErrorMessage.SYNC_HUB_DATA_HOST_AES_KEY_EMPTY.getMessage());
        }
      }
    }
    checkConfig = false;
  }
}
