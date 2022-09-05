// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.DeletePermissionReq;
import com.reddate.hub.hub.dto.req.DeleteResourceReq;
import com.reddate.hub.hub.dto.req.QueryResourceHistoryReq;
import com.reddate.hub.hub.dto.req.QueryResourceReq;
import com.reddate.hub.hub.dto.req.SaveResourceReq;
import com.reddate.hub.hub.dto.req.TransferOwnerReq;
import com.reddate.hub.hub.dto.resp.DeleteResourceResp;
import com.reddate.hub.hub.dto.resp.PermissionInfo;
import com.reddate.hub.hub.dto.resp.QueryPermissionResp;
import com.reddate.hub.hub.dto.resp.QueryResourceHistoryResp;
import com.reddate.hub.hub.dto.resp.QueryResourceResp;
import com.reddate.hub.hub.dto.resp.ResourceHistoryInfo;
import com.reddate.hub.hub.dto.resp.SaveResourceResp;
import com.reddate.hub.hub.dto.resp.TransferOwnerResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.hub.param.req.UsedFlag;
import com.reddate.hub.server.constant.HubConstat;
import com.reddate.hub.server.service.PermissionService;
import com.reddate.hub.server.service.ResourceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

  public static final String RESOURCE_COLLECT_NAME = "resourceInfo";
  public static final String RESOURCE_HISTORY_NAME = "resourceHis";

  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private PermissionService permissionService;

  @Override
  public SaveResourceResp saveResource(String uid, SaveResourceReq resourceReq) {
    if (StringUtils.isEmpty(resourceReq.getEnContent())) {
      throw new IdentityHubException(
          ErrorMessage.CONTNET_EMPTY.getCode(), ErrorMessage.CONTNET_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(resourceReq.getOwnerUid())) {
      throw new IdentityHubException(
          ErrorMessage.USERID_EMPTY.getCode(), ErrorMessage.USERID_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(resourceReq.getGrant())) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_NOT_NULL.getCode(), ErrorMessage.GRANT_NOT_NULL.getMessage());
    }

    if (!(resourceReq.getGrant().equals(Operation.WRITE.toString())
        || resourceReq.getGrant().equals(Operation.UPDATE.toString()))) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_ERROR.getCode(), ErrorMessage.GRANT_ERROR.getMessage());
    }

    if (resourceReq.getGrant().equals(Operation.WRITE.toString())
        && uid.equals(resourceReq.getOwnerUid())) {
      String url = resourceReq.getOwnerUid() + UUID.randomUUID().toString().replace("-", "");
      resourceReq.setUrl(url);
    } else {
      if (StringUtils.isEmpty(resourceReq.getUrl())) {
        throw new IdentityHubException(
            ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
      }
    }

    SaveResourceResp resp = new SaveResourceResp();
    // checck permission
    PermissionInfo permissionInfo = null;
    if (!uid.equals(resourceReq.getOwnerUid())) {
      permissionInfo =
          permissionService.getPermission(
              resourceReq.getOwnerUid(), uid, resourceReq.getUrl(), resourceReq.getGrant());
      if (permissionInfo == null
          || permissionInfo.getKey() == null
          || permissionInfo.getKey().trim().isEmpty()) {
        if ("WRITE".equalsIgnoreCase(resourceReq.getGrant())) {
          throw new IdentityHubException(
              ErrorMessage.NO_PERMSSION_ADD_RESOUIRCE.getCode(),
              ErrorMessage.NO_PERMSSION_ADD_RESOUIRCE.getMessage());
        } else {
          throw new IdentityHubException(
              ErrorMessage.NO_PERMSSION_UPDATE_RESOUIRCE.getCode(),
              ErrorMessage.NO_PERMSSION_UPDATE_RESOUIRCE.getMessage());
        }
      }
    }

    Map<String, Object> saveHisMap = new HashMap<>();
    saveHisMap.put("operation", resourceReq.getGrant());

    String now = LocalDateTime.now().toString();
    Query query =
        new Query(
            Criteria.where("url")
                .is(resourceReq.getUrl())
                .and("ownerUid")
                .is(resourceReq.getOwnerUid()));
    Map<String, Object> queryMap = mongoTemplate.findOne(query, Map.class, RESOURCE_COLLECT_NAME);
    // save resouce
    if ("WRITE".equalsIgnoreCase(resourceReq.getGrant())) {
      if (queryMap != null) {
        throw new IdentityHubException(
            ErrorMessage.CAN_NOT_SAVE_RESOURCE_AGAIN.getCode(),
            ErrorMessage.CAN_NOT_SAVE_RESOURCE_AGAIN.getMessage());
      }

      Map<String, Object> saveMap = new HashMap<>();
      saveMap.put("content", resourceReq.getEnContent());
      saveMap.put("ownerUid", resourceReq.getOwnerUid());
      saveMap.put("operationTime", now);
      saveMap.put("operationUid", uid);
      saveMap.put("createTime", now);
      saveMap.put("url", resourceReq.getUrl());
      

      saveHisMap.put("content", saveMap.get("content"));
      saveHisMap.put("ownerUid", saveMap.get("ownerUid"));
      saveHisMap.put("operationTime", saveMap.get("operationTime"));
      saveHisMap.put("operationUid", saveMap.get("operationUid"));
      saveHisMap.put("url", saveMap.get("url"));
      

      if (uid.equals(resourceReq.getOwnerUid())) {
        saveMap.put("key", resourceReq.getEnKey());
        saveHisMap.put("key", saveMap.get("key"));
      } else {
        saveMap.put("key", permissionInfo.getOwnerKey());
        saveHisMap.put("key", saveMap.get("key"));
      }

      saveMap.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());

      Map<String, Object> saveMapResult = mongoTemplate.save(saveMap, RESOURCE_COLLECT_NAME);
      if (saveMapResult == null || saveMapResult.get("url") == null) {
        throw new IdentityHubException(
            ErrorMessage.SAVE_RESOURCE_ERROR.getCode(),
            ErrorMessage.SAVE_RESOURCE_ERROR.getMessage());
      }
      resp.setUrl(String.valueOf(saveMapResult.get("url")));
    } else {
      if (queryMap == null) {
        throw new IdentityHubException(
            ErrorMessage.RESOURCE_NOT_FOUND.getCode(),
            ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
      }
      Update update =
          Update.update("content", resourceReq.getEnContent())
          	  .set("key", resourceReq.getEnKey())
              .set("operationTime", now)
              .set("operationUid", uid);
      update = update.set(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());

      saveHisMap.put("content", resourceReq.getEnContent());
      saveHisMap.put("operationTime", now);
      saveHisMap.put("operationUid", uid);
      saveHisMap.put("ownerUid", queryMap.get("ownerUid"));
      saveHisMap.put("url", queryMap.get("url"));
      saveHisMap.put("key", resourceReq.getEnKey());

      UpdateResult updateResult = mongoTemplate.updateMulti(query, update, RESOURCE_COLLECT_NAME);
      if (updateResult == null || updateResult.getModifiedCount() == 0) {
        throw new IdentityHubException(
            ErrorMessage.UPDATE_RESOURCE_FAILED.getCode(),
            ErrorMessage.UPDATE_RESOURCE_FAILED.getMessage());
      }
      resp.setUrl(String.valueOf(queryMap.get("url")));
    }

    saveHisMap.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
    // add the resouce handle event
    Map<String, Object> saveHisResult = mongoTemplate.save(saveHisMap, RESOURCE_HISTORY_NAME);
    if (saveHisResult == null) {
      throw new IdentityHubException(
          ErrorMessage.ADD_OPERATION_HISTORY_FAILED.getCode(),
          ErrorMessage.ADD_OPERATION_HISTORY_FAILED.getMessage());
    }

    // close permission event
    if (!uid.equals(resourceReq.getOwnerUid())) {
      Criteria criteria =
          Criteria.where("grantUid")
              .is(uid)
              .and("url")
              .is(resourceReq.getUrl())
              .and("grant")
              .is(resourceReq.getGrant())
              .and("uid")
              .is(resourceReq.getOwnerUid())
              .and("status")
              .is(1)
              .and("flag")
              .is(1);
      Query querys = new Query(criteria);
      Update updates = Update.update("flag", 0).set("readTime", now);
      updates = updates.set(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
      UpdateResult updatePerResult =
          mongoTemplate.updateMulti(querys, updates, PermissionServiceImpl.PERMISSION_NAME);
      if (updatePerResult == null || updatePerResult.getModifiedCount() == 0) {
        throw new IdentityHubException(
            ErrorMessage.CLOSE_PERMISSION_ERROR.getCode(),
            ErrorMessage.CLOSE_PERMISSION_ERROR.getMessage());
      }
    }

    return resp;
  }

  @Override
  public QueryResourceResp getResource(String uid, QueryResourceReq queryResourceReq) {
    Query query = new Query(Criteria.where("url").is(queryResourceReq.getUrl()));
    Map map = mongoTemplate.findOne(query, Map.class, RESOURCE_COLLECT_NAME);
    if (map == null) {
      throw new IdentityHubException(
          ErrorMessage.RESOURCE_NOT_FOUND.getCode(), ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
    }
    QueryResourceResp resp = new QueryResourceResp();
    String resourceOwnerUid = (String) map.get("ownerUid");
    if (uid.equals(resourceOwnerUid)) {
      resp.setContent(map.get("content").toString());
      resp.setKey(map.get("key").toString());
    } else {
      Query permQuery =
          new Query(
              Criteria.where("uid")
                  .is(resourceOwnerUid)
                  .and("grantUid")
                  .is(uid)
                  .and("url")
                  .is(queryResourceReq.getUrl())
                  .and("grant")
                  .is(Operation.READ.toString())
                  .and("status")
                  .is(1)
                  .and("flag")
                  .is(1));
      Map perMap =
          mongoTemplate.findOne(permQuery, Map.class, PermissionServiceImpl.PERMISSION_NAME);
      if (perMap == null) {
        throw new IdentityHubException(
            ErrorMessage.PERMISSION_NOT_FOUND.getCode(),
            ErrorMessage.PERMISSION_NOT_FOUND.getMessage());
      }
      resp.setContent(map.get("content").toString());
      resp.setKey(perMap.get("key").toString());

      Update update = Update.update("flag", 0).set("readTime", LocalDateTime.now().toString());
      update = update.set(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
      UpdateResult updateResult =
          mongoTemplate.updateMulti(permQuery, update, PermissionServiceImpl.PERMISSION_NAME);
      if (updateResult == null || updateResult.getModifiedCount() == 0) {
        throw new IdentityHubException(
            ErrorMessage.CLOSE_PERMISSION_ERROR.getCode(),
            ErrorMessage.CLOSE_PERMISSION_ERROR.getMessage());
      }
    }

    return resp;
  }

  @Override
  public DeleteResourceResp deleteResource(String uid, DeleteResourceReq deleteResourceReq) {
    Query querys = new Query(Criteria.where("url").is(deleteResourceReq.getUrl()));
    Map<String, Object> maps = mongoTemplate.findOne(querys, Map.class, RESOURCE_COLLECT_NAME);
    if (maps == null) {
      throw new IdentityHubException(
          ErrorMessage.RESOURCE_NOT_FOUND.getCode(), ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    String resourceOwnerUid = (String) maps.get("ownerUid");
    if (!uid.equals(resourceOwnerUid)) {
      throw new IdentityHubException(
          ErrorMessage.NOT_OWNER_DELETE.getCode(), ErrorMessage.NOT_OWNER_DELETE.getMessage());
    }

    maps.put("operation", Operation.DELETE);
    maps.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
    Map<String, Object> saveHisMap = mongoTemplate.save(maps, RESOURCE_HISTORY_NAME);
    if (saveHisMap == null || saveHisMap.isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.ADD_OPERATE_ERROR.getCode(), ErrorMessage.ADD_OPERATE_ERROR.getMessage());
    }

    DeleteResourceResp deleteResourceResp = new DeleteResourceResp();
    Criteria criteria =
        Criteria.where("url").is(deleteResourceReq.getUrl()).and("ownerUid").is(resourceOwnerUid);
    Query query = new Query(criteria);
    DeleteResult deleteResult = mongoTemplate.remove(query, Map.class, RESOURCE_COLLECT_NAME);
    if (deleteResult.getDeletedCount() == 1) {
      deleteResourceResp.setSucces(true);
    } else {
      deleteResourceResp.setSucces(false);
    }
    return deleteResourceResp;
  }

  @Override
  public List<QueryResourceResp> queryResource(String uid) {
    Query query = new Query(Criteria.where("ownerUid").is(uid));
    List<QueryResourceResp> queryResourceResps = new ArrayList<>();
    List<Map> maps = mongoTemplate.find(query, Map.class, RESOURCE_COLLECT_NAME);
    if (maps != null) {
      for (Map map : maps) {
        QueryResourceResp resp = new QueryResourceResp();
        resp.setOwnerUid(map.get("ownerUid").toString());
        resp.setContent(map.get("content").toString());
        resp.setKey(map.get("key").toString());
        resp.setUrl(map.get("url").toString());
        queryResourceResps.add(resp);
      }
    }
    return queryResourceResps;
  }

  @Override
  public void updateResource(String url, String key) {
    Query query = new Query(Criteria.where("url").is(url));
    Update update = Update.update("key", key);
    update = update.set(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, RESOURCE_COLLECT_NAME);
  }

  @Override
  public QueryResourceHistoryResp queryResourceHistory(
      String uid, QueryResourceHistoryReq queryResourceHisReq) {
    Criteria criteria = Criteria.where("ownerUid").is(uid);
    if (queryResourceHisReq.getUrl() != null && !queryResourceHisReq.getUrl().trim().isEmpty()) {
      criteria = criteria.and("url").is(queryResourceHisReq.getUrl());
    }
    if (queryResourceHisReq.getGrant() != null
        && !queryResourceHisReq.getGrant().trim().isEmpty()) {
      criteria = criteria.and("operation").is(queryResourceHisReq.getGrant());
    }
    Query query = new Query(criteria);
    List<Order> orders = new ArrayList<>();
    orders.add(new Order(Sort.Direction.ASC, "url"));
    orders.add(new Order(Sort.Direction.DESC, "operationTime"));
    query = query.with(Sort.by(orders));

    List<Map> mapList = mongoTemplate.find(query, Map.class, RESOURCE_HISTORY_NAME);
    QueryResourceHistoryResp historyResp = new QueryResourceHistoryResp();
    historyResp.setList(new ArrayList<ResourceHistoryInfo>());
    if (mapList != null) {
      for (Map map : mapList) {
        ResourceHistoryInfo his = new ResourceHistoryInfo();
        his.setOperationUid((String) map.get("operationUid"));
        his.setOwnerUid((String) map.get("ownerUid"));
        his.setOperation((String) map.get("operation"));
        his.setContent((String) map.get("content"));
        his.setUrl((String) map.get("url"));
        his.setKey((String) map.get("key"));
        if (map.get("operationTime") != null) {
          his.setOperationTime(LocalDateTime.parse(map.get("operationTime").toString()));
        }
        historyResp.getList().add(his);
      }
    }
    return historyResp;
  }

  @Override
  public TransferOwnerResp transferOwner(String uid, TransferOwnerReq transferOwnerReq) {
    if (StringUtils.isEmpty(transferOwnerReq.getUrl())) {
      throw new IdentityHubException(
          ErrorMessage.URL_EMPTY.getCode(), ErrorMessage.URL_EMPTY.getMessage());
    }

    if (StringUtils.isEmpty(transferOwnerReq.getNewOwnerUid())) {
      throw new IdentityHubException(
          ErrorMessage.NEW_OWNER_UID_EMPTY.getCode(),
          ErrorMessage.NEW_OWNER_UID_EMPTY.getMessage());
    }

    if (uid.equals(transferOwnerReq.getNewOwnerUid())) {
      throw new IdentityHubException(
          ErrorMessage.UID_OWNERUID_EQ_ERROR.getCode(),
          ErrorMessage.UID_OWNERUID_EQ_ERROR.getMessage());
    }

    if (StringUtils.isEmpty(transferOwnerReq.getNewKey())) {
      throw new IdentityHubException(
          ErrorMessage.KEY_IS_EMPTY.getCode(), ErrorMessage.KEY_IS_EMPTY.getMessage());
    }

    String now = LocalDateTime.now().toString();
    Query query =
        new Query(Criteria.where("url").is(transferOwnerReq.getUrl()).and("ownerUid").is(uid));
    Map<String, Object> queryMap = mongoTemplate.findOne(query, Map.class, RESOURCE_COLLECT_NAME);
    if (queryMap == null || queryMap.isEmpty()) {
      throw new IdentityHubException(
          ErrorMessage.RESOURCE_NOT_FOUND.getCode(),
          ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    QueryPermissionResp queryPermissionResp =
        permissionService.getAllPermission(uid, null, UsedFlag.NO.ordinal());
    List<PermissionInfo> authList = queryPermissionResp.getAuthList();
    if (authList != null) {
      for (int i = 0; i < authList.size(); i++) {
        if(transferOwnerReq.getUrl().equals(authList.get(i).getUrl())) {
          DeletePermissionReq deletePermissionReq = new DeletePermissionReq();
      	  deletePermissionReq.setUrl(authList.get(i).getUrl());
      	  deletePermissionReq.setGrantUid(authList.get(i).getGrantUid());
      	  deletePermissionReq.setGrant(authList.get(i).getGrant());
      	  permissionService.deletePermission(uid, deletePermissionReq);
        }
      }
    }

    Update update =
        Update.update("ownerUid", transferOwnerReq.getNewOwnerUid())
            .set("key", transferOwnerReq.getNewKey())
            .set("operationTime", now)
            .set("operationUid", uid);
    update = update.set(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, RESOURCE_COLLECT_NAME);
    if (updateResult == null || updateResult.getModifiedCount() == 0) {
      throw new IdentityHubException(
          ErrorMessage.TRANSFER_OWNER_ERROR.getCode(),
          ErrorMessage.TRANSFER_OWNER_ERROR.getMessage());
    }

    Map<String, Object> saveHisMap = new HashMap<>();
    saveHisMap.put("operation", Operation.TRANSFER.toString());
    saveHisMap.put("content", queryMap.get("content"));
    saveHisMap.put("ownerUid", transferOwnerReq.getNewOwnerUid());
    saveHisMap.put("operationTime", now);
    saveHisMap.put("operationUid", uid);
    saveHisMap.put("url", queryMap.get("url"));
    saveHisMap.put("key", transferOwnerReq.getNewKey());
    saveHisMap.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());

    // add the resouce handle event
    Map<String, Object> saveHisResult = mongoTemplate.save(saveHisMap, RESOURCE_HISTORY_NAME);
    if (saveHisResult == null) {
      throw new IdentityHubException(
          ErrorMessage.ADD_OPERATION_HISTORY_FAILED.getCode(),
          ErrorMessage.ADD_OPERATION_HISTORY_FAILED.getMessage());
    }

    TransferOwnerResp transferOwnerResp = new TransferOwnerResp();
    transferOwnerResp.setSucces(true);
    return transferOwnerResp;
  }
}
