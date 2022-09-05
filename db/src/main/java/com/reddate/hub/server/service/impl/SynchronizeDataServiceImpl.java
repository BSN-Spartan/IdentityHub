// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.server.constant.HubConstat;
import com.reddate.hub.server.dto.FieldInfo;
import com.reddate.hub.server.dto.FieldType;
import com.reddate.hub.server.dto.HubMongoRecord;
import com.reddate.hub.server.dto.SyncDataReq;
import com.reddate.hub.server.dto.SyncDataResp;
import com.reddate.hub.server.service.SynchronizeDataService;

@Service
public class SynchronizeDataServiceImpl implements SynchronizeDataService {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public List<HubMongoRecord> querySynchronizeData(LocalDateTime lastSyncTime, LocalDateTime now) {
    List<HubMongoRecord> dataList = new ArrayList<>();

    Criteria criteria1 = Criteria.where(HubConstat.LAST_UPDATE_TIME).gte(lastSyncTime);
    Criteria criteria2 = Criteria.where(HubConstat.LAST_UPDATE_TIME).lte(now);
    Query query = new Query(new Criteria().andOperator(criteria1, criteria2));

    List<Map> connList = mongoTemplate.find(query, Map.class, ConnServiceImpl.HUB_CONN_COLLLECT);
    this.addRecordToList(connList, ConnServiceImpl.HUB_CONN_COLLLECT, dataList);

    List<Map> resList =
        mongoTemplate.find(query, Map.class, ResourceServiceImpl.RESOURCE_COLLECT_NAME);
    this.addRecordToList(resList, ResourceServiceImpl.RESOURCE_COLLECT_NAME, dataList);

    List<Map> resHisList =
        mongoTemplate.find(query, Map.class, ResourceServiceImpl.RESOURCE_HISTORY_NAME);
    this.addRecordToList(resHisList, ResourceServiceImpl.RESOURCE_HISTORY_NAME, dataList);

    List<Map> pemList = mongoTemplate.find(query, Map.class, PermissionServiceImpl.PERMISSION_NAME);
    this.addRecordToList(pemList, PermissionServiceImpl.PERMISSION_NAME, dataList);

    return dataList;
  }

  private void addRecordToList(
      List<Map> dataMapList, String collectionName, List<HubMongoRecord> list) {
    if (dataMapList != null && dataMapList.size() > 0) {
      for (int i = 0; i < dataMapList.size(); i++) {
        HubMongoRecord hubMongoRecord = new HubMongoRecord();
        hubMongoRecord.setTableName(collectionName);
        Map connMap = dataMapList.get(i);
        Iterator it = connMap.keySet().iterator();
        while (it.hasNext()) {
          String colName = (String) it.next();
          if ("_id".equals(colName)) {
            continue;
          }
          Object colValues = connMap.get(colName);
          if (colValues == null) {
            continue;
          }
          FieldType colType = FieldType.STRING;
          String colVal = null;
          if (colValues instanceof Date) {
            java.util.Date tt1 = (java.util.Date) colValues;
            LocalDateTime colVal11 =
                tt1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            colVal = colVal11.toString();
            colType = FieldType.LOCALDATETIME;
          } else if (colValues instanceof Integer) {
            Integer tt11 = (Integer) colValues;
            colVal = tt11.toString();
            colType = FieldType.INTEGER;
          } else {
            colVal = (String) connMap.get(colName);
          }
          hubMongoRecord.getFields().add(new FieldInfo(colName, colVal, colType.ordinal()));
        }
        list.add(hubMongoRecord);
      }
    }
  }

  @Override
  public SyncDataResp processSyncData(SyncDataReq vo, String aesKey) {
    SyncDataResp syncDataResp = new SyncDataResp();

    String plaintextData = null;
    try {
      plaintextData = AesUtils.decrypt(vo.getData(), aesKey);
    } catch (Exception e) {
      e.printStackTrace();
      syncDataResp.setCode(ErrorMessage.SYNC_HUB_DATA_DECRYPT_FAILED.getCode());
      syncDataResp.setMsg(ErrorMessage.SYNC_HUB_DATA_DECRYPT_FAILED.getMessage());
      return syncDataResp;
    }

    List<HubMongoRecord> dataList = JSON.parseArray(plaintextData, HubMongoRecord.class);
    for (int i = 0; i < dataList.size(); i++) {
      HubMongoRecord hubMongoRecord = dataList.get(i);
      Map<String, Object> map = new HashMap<>();
      for (FieldInfo field : hubMongoRecord.getFields()) {
        if (FieldType.INTEGER.ordinal() == field.getType()) {
          map.put(field.getName(), Integer.parseInt(field.getVal()));
        } else if (FieldType.LOCALDATETIME.ordinal() == field.getType()) {
          map.put(field.getName(), LocalDateTime.parse(field.getVal()));
        } else {
          map.put(field.getName(), field.getVal());
        }
      }

      Query query = null;
      List<String> queryColumnList = new ArrayList<>();
      if (ConnServiceImpl.HUB_CONN_COLLLECT.equals(hubMongoRecord.getTableName())) {
        // connInfo -> uid
        query = new Query(Criteria.where("uid").is(map.get("uid")));
        queryColumnList.add("uid");
      }

      if (ResourceServiceImpl.RESOURCE_COLLECT_NAME.equals(hubMongoRecord.getTableName())) {
        // resourceInfo -> url
        query = new Query(Criteria.where("url").is(map.get("url")));
        queryColumnList.add("url");
      }

      if (ResourceServiceImpl.RESOURCE_HISTORY_NAME.equals(hubMongoRecord.getTableName())) {
        // resourceHis -> url,operationTime
        query =
            new Query(
                Criteria.where("url")
                    .is(map.get("url"))
                    .and("operationTime")
                    .is(map.get("operationTime")));
        queryColumnList.add("url");
        queryColumnList.add("operationTime");
      }

      if (PermissionServiceImpl.PERMISSION_NAME.equals(hubMongoRecord.getTableName())) {
        // permissionInfo -> url,grantUid,grant
        query =
            new Query(
                Criteria.where("url")
                    .is(map.get("url"))
                    .and("grantUid")
                    .is(map.get("grantUid"))
                    .and("grant")
                    .is(map.get("grant")));
        queryColumnList.add("url");
        queryColumnList.add("grantUid");
        queryColumnList.add("grant");
      }

      // save or update data to db
      Long existRows = mongoTemplate.count(query, hubMongoRecord.getTableName());
      if (existRows == 0) {
        mongoTemplate.insert(map, hubMongoRecord.getTableName());
      } else {
        Update update = null;
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
          String colName = it.next();
          if (queryColumnList.contains(colName)) {
            continue;
          }
          if (update == null) {
            update = Update.update(colName, map.get(colName));
          } else {
            update.set(colName, map.get(colName));
          }
        }
        mongoTemplate.updateMulti(query, update, hubMongoRecord.getTableName());
      }
    }

    syncDataResp.setCode(ErrorMessage.SUCCESS.getCode());
    syncDataResp.setMsg(ErrorMessage.SUCCESS.getMessage());
    return syncDataResp;
  }
}
