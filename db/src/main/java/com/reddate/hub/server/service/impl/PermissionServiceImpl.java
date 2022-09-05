// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.AddPermissionReq;
import com.reddate.hub.hub.dto.req.DeletePermissionReq;
import com.reddate.hub.hub.dto.resp.AddPermissionResp;
import com.reddate.hub.hub.dto.resp.DeletePermissionResp;
import com.reddate.hub.hub.dto.resp.GrantPermissionInfo;
import com.reddate.hub.hub.dto.resp.PermissionInfo;
import com.reddate.hub.hub.dto.resp.QueryGantPermissionResp;
import com.reddate.hub.hub.dto.resp.QueryPermissionResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.hub.param.req.UsedFlag;
import com.reddate.hub.server.constant.HubConstat;
import com.reddate.hub.server.service.PermissionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

  public static final String PERMISSION_NAME = "permissionInfo";

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public AddPermissionResp addPermission(String uid, AddPermissionReq permissionReq) {
    if (uid.equals(permissionReq.getGrantUid())) {
      throw new IdentityHubException(
          ErrorMessage.NO_NEED_ADD_PERMISSION.getCode(), ErrorMessage.GRANT_ERROR.getMessage());
    }

    if (!(permissionReq.getGrant().equals(Operation.WRITE.toString())
        || permissionReq.getGrant().equals(Operation.UPDATE.toString())
        || permissionReq.getGrant().equals(Operation.READ.toString()))) {
      throw new IdentityHubException(
          ErrorMessage.GRANT_ERROR.getCode(), ErrorMessage.GRANT_ERROR.getMessage());
    }

    if (permissionReq.getGrant().equals(Operation.WRITE.toString())) {
      String url = uid + UUID.randomUUID().toString().replace("-", "");
      permissionReq.setUrl(url);
    }

    AddPermissionResp addPermissionResp = new AddPermissionResp();
    Map<String, Object> map = new HashMap<>();
    PermissionInfo permissionInfo =
        getPermission(
            permissionReq.getUid(),
            permissionReq.getGrantUid(),
            permissionReq.getUrl(),
            permissionReq.getGrant());
    if (permissionInfo.getUrl() != null) {
      addPermissionResp.setUrl(permissionInfo.getUrl());
      addPermissionResp.setKey(permissionInfo.getKey());
      addPermissionResp.setGrant(permissionInfo.getGrant());
      addPermissionResp.setUid(permissionInfo.getGrantUid());
      return addPermissionResp;
    }

    if (permissionReq.getGrant().equals(Operation.UPDATE.toString())
        || permissionReq.getGrant().equals(Operation.READ.toString())) {
      Query query =
          new Query(Criteria.where("ownerUid").is(uid).and("url").is(permissionReq.getUrl()));
      Map resourceMap =
          mongoTemplate.findOne(query, Map.class, ResourceServiceImpl.RESOURCE_COLLECT_NAME);
      if (resourceMap == null) {
        throw new IdentityHubException(
            ErrorMessage.RESOURCE_NOT_FOUND.getCode(),
            ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
      }
    } else {
      Query query =
          new Query(
              Criteria.where("uid")
                  .is(uid)
                  .and("grantUid")
                  .is(permissionReq.getGrantUid())
                  .and("grant")
                  .is(permissionReq.getGrant())
                  .and("flag")
                  .is(1)
                  .and("status")
                  .is(1));
      long permssionCnt = mongoTemplate.count(query, Map.class, PERMISSION_NAME);
      if (permssionCnt > 0) {
        throw new IdentityHubException(
            ErrorMessage.EXISTS_WRITE_AGAIN.getCode(),
            ErrorMessage.EXISTS_WRITE_AGAIN.getMessage());
      }
    }

    map.put("url", permissionReq.getUrl());
    map.put("grant", permissionReq.getGrant());
    map.put("uid", permissionReq.getUid());
    map.put("createTime", LocalDateTime.now().toString());
    map.put("flag", 1);
    map.put("readTime", null);
    map.put("status", 1);
    map.put("key", permissionReq.getKey());
    if (permissionReq.getGrant().equals(Operation.WRITE.toString())) {
      map.put("ownerKey", permissionReq.getKey1());
    }
    map.put("grantUid", permissionReq.getGrantUid());
    map.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
    Map<String, Object> insertMap = mongoTemplate.save(map, PERMISSION_NAME);
    addPermissionResp.setUrl(insertMap.get("url").toString());
    addPermissionResp.setKey(insertMap.get("key").toString());
    addPermissionResp.setGrant(insertMap.get("grant").toString());
    addPermissionResp.setUid(insertMap.get("grantUid").toString());
    return addPermissionResp;
  }

  @Override
  public DeletePermissionResp deletePermission(
      String uid, DeletePermissionReq deletePermissionReq) {
    DeletePermissionResp deletePermissionResp = new DeletePermissionResp();
    Criteria criteria =
        Criteria.where("url")
            .is(deletePermissionReq.getUrl())
            .and("grant")
            .is(deletePermissionReq.getGrant())
            .and("grantUid")
            .is(deletePermissionReq.getGrantUid())
            .and("status")
            .is(1)
            .and("uid")
            .is(uid)
            .and("flag")
            .is(1);
    Query query = new Query(criteria);
    Map<String, Object> map = mongoTemplate.findOne(query, Map.class, PERMISSION_NAME);
    if (map != null) {
      Update update = Update.update("status", 0);
      update = update.set(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
      UpdateResult updateResult = mongoTemplate.updateMulti(query, update, PERMISSION_NAME);
      if (updateResult.getModifiedCount() != 1) {
        deletePermissionResp.setSucces(false);
        deletePermissionResp.setMessage(
            ErrorMessage.DELETE_PERMISSION_FAILED.getCode()
                + "-"
                + ErrorMessage.DELETE_PERMISSION_FAILED.getMessage());
        return deletePermissionResp;
      }
      deletePermissionResp.setSucces(true);
      return deletePermissionResp;
    }
    deletePermissionResp.setSucces(false);
    deletePermissionResp.setMessage(
        ErrorMessage.PERMISSION_NOT_FOUND.getCode()
            + "-"
            + ErrorMessage.PERMISSION_NOT_FOUND.getMessage());
    return deletePermissionResp;
  }

  @Override
  public QueryPermissionResp getAllPermission(String uid, String grantUid, Integer flag) {

    Criteria criteria = Criteria.where("uid").is(uid).and("status").is(1);
    if (grantUid != null && !grantUid.trim().isEmpty()) {
      criteria = criteria.and("grantUid").is(grantUid);
    }
    if (flag != null && flag != 2) {
      criteria = criteria.and("flag").is(flag);
    }
    Query query = new Query(criteria);

    List<Map> maps = mongoTemplate.find(query, Map.class, PERMISSION_NAME);
    List<PermissionInfo> list = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      PermissionInfo permissionInfo = new PermissionInfo();
      permissionInfo.setGrantUid(map.get("grantUid").toString());
      permissionInfo.setStatus((Integer) map.get("status"));
      permissionInfo.setUrl(map.get("url").toString());
      permissionInfo.setGrant(map.get("grant").toString());
      if (map.get("createTime") != null) {
        permissionInfo.setCreateTime(LocalDateTime.parse(map.get("createTime").toString()));
      }
      if (map.get("readTime") != null) {
        permissionInfo.setReadTime(LocalDateTime.parse(map.get("readTime").toString()));
      }
      permissionInfo.setUid(map.get("uid").toString());
      Integer usedFlag = (Integer) map.get("flag");
      if (usedFlag != null) {
        permissionInfo.setFlag(UsedFlag.ofValue(usedFlag));
      }
      permissionInfo.setKey(map.get("key").toString());
      list.add(permissionInfo);
    }
    QueryPermissionResp resp = new QueryPermissionResp();
    resp.setAuthList(list);
    return resp;
  }

  @Override
  public PermissionInfo getPermission(String uid, String granteeUid, String url, String grant) {
    Criteria criteria = null;
    PermissionInfo permissionInfo = new PermissionInfo();
    criteria =
        Criteria.where("grantUid")
            .is(granteeUid)
            .and("grant")
            .is(grant)
            .and("uid")
            .is(uid)
            .and("status")
            .is(1)
            .and("flag")
            .is(1);
    if (!StringUtils.isEmpty(url)) {
      criteria = criteria.and("url").is(url);
    }

    Query query = new Query(criteria);
    Map map = mongoTemplate.findOne(query, Map.class, PERMISSION_NAME);
    if (map == null) {
      return permissionInfo;
    }
    permissionInfo.setGrantUid(String.valueOf(map.get("grantUid")));
    permissionInfo.setGrant(String.valueOf(map.get("grant")));
    permissionInfo.setKey(String.valueOf(map.get("key")));
    permissionInfo.setUrl(String.valueOf(map.get("url")));
    permissionInfo.setOwnerKey(String.valueOf(map.get("ownerKey")));
    return permissionInfo;
  }

  @Override
  public QueryGantPermissionResp getAllGrantedPermission(
      String uid, String ownerUid, String grant, Integer flag) {
    Criteria criteria = Criteria.where("grantUid").is(uid).and("status").is(1);
    if (ownerUid != null && !ownerUid.trim().isEmpty()) {
      criteria = criteria.and("uid").is(ownerUid);
    }
    if (flag != null && flag != 2) {
      criteria = criteria.and("flag").is(flag);
    }
    if (grant != null && !grant.trim().isEmpty()) {
      criteria = criteria.and("grant").is(grant);
    }

    Query query = new Query(criteria);
    query = query.with(Sort.by(Sort.Direction.DESC, "lastUpdTime"));
    List<Map> maps = mongoTemplate.find(query, Map.class, PERMISSION_NAME);
    List<GrantPermissionInfo> list = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      GrantPermissionInfo permissionInfo = new GrantPermissionInfo();
      permissionInfo.setStatus((Integer) map.get("status"));
      permissionInfo.setUrl(map.get("url").toString());
      permissionInfo.setGrant(map.get("grant").toString());
      if (map.get("createTime") != null) {
        permissionInfo.setCreateTime(LocalDateTime.parse(map.get("createTime").toString()));
      }
      if (map.get("readTime") != null) {
        permissionInfo.setReadTime(LocalDateTime.parse(map.get("readTime").toString()));
      }
      permissionInfo.setOwnerUid(map.get("uid").toString());
      Integer usedFlag = (Integer) map.get("flag");
      if (usedFlag != null) {
        permissionInfo.setFlag(UsedFlag.ofValue(usedFlag));
      }
      permissionInfo.setKey(map.get("key").toString());
      if (map.get("ownerKey") != null) {
        permissionInfo.setOwnerKey(map.get("ownerKey").toString());
      }
      list.add(permissionInfo);
    }
    QueryGantPermissionResp resp = new QueryGantPermissionResp();
    resp.setAuthList(list);
    return resp;
  }
}
