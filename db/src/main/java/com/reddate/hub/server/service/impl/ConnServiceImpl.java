// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.RegisterHubReq;
import com.reddate.hub.hub.dto.resp.RegisterHubResp;
import com.reddate.hub.server.constant.HubConstat;
import com.reddate.hub.server.service.ConnService;

@Service
public class ConnServiceImpl implements ConnService {

  public static final String HUB_CONN_COLLLECT = "connInfo";

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public RegisterHubResp registerHub(String uid, RegisterHubReq connHubReq) {
    RegisterHubResp connHubResp = new RegisterHubResp();
    if (connHubReq == null) {
      connHubResp.setSuccess(false);
    }

    String publicKey = connHubReq.getPublicKey();
    if (publicKey == null || publicKey.trim().isEmpty()) {
      connHubResp.setSuccess(false);
    }

    String existPublicKey = this.queryPublicKey(uid);
    String existUid = this.queryUidByPublicKey(publicKey);
    if ((existPublicKey != null && !existPublicKey.trim().isEmpty()) 
    		|| (existUid != null && !existUid.trim().isEmpty())) {
      connHubResp.setSuccess(false);
      connHubResp.setMessage(
          ErrorMessage.USER_RESITERD.getCode() + "-" + ErrorMessage.USER_RESITERD.getMessage());
      return connHubResp;
    }

    Map<String, Object> map = new HashMap<>();
    map.put("uid", uid);
    map.put("publicKey", publicKey);
    map.put("cryptoType", connHubReq.getCryptoType());
    map.put("createTime", LocalDateTime.now());
    map.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());
    Map<String, Object> saveConn = mongoTemplate.save(map, HUB_CONN_COLLLECT);
    if (saveConn != null && saveConn.get("uid") != null) {
      connHubResp.setSuccess(true);
    } else {
      connHubResp.setSuccess(false);
      connHubResp.setMessage(
          ErrorMessage.SAVE_HUB_USER_FAILED.getCode()
              + "-"
              + ErrorMessage.SAVE_HUB_USER_FAILED.getMessage());
    }
    return connHubResp;
  }

  @Override
  public String queryPublicKey(String uid) {
    Query query = new Query();
    query.addCriteria(Criteria.where("uid").is(uid));
    Map map = mongoTemplate.findOne(query, Map.class, HUB_CONN_COLLLECT);
    if (map != null) {
      String publicKey = (String) map.get("publicKey");
      return publicKey;
    }
    return null;
  }

  @Override
  public String queryUidByPublicKey(String publicKey) {
    Query query = new Query();
    query.addCriteria(Criteria.where("publicKey").is(publicKey));
    Map map = mongoTemplate.findOne(query, Map.class, HUB_CONN_COLLLECT);
    if (map != null) {
      String uid = (String) map.get("uid");
      return uid;
    }
    return null;
  }

  @Override
  public Integer getCryptoTypeByPublicKey(String regUid,String publicKey) {
    Query query = new Query();
    if(regUid != null && !regUid.trim().isEmpty()) {
    	query.addCriteria(Criteria.where("uid").is(regUid));
    }else {
    	query.addCriteria(Criteria.where("publicKey").is(publicKey));
    }
    Map map = mongoTemplate.findOne(query, Map.class, HUB_CONN_COLLLECT);
    if (map != null) {
      Integer cryptoType = (Integer) map.get("cryptoType");
      if(cryptoType == null) {
    	  cryptoType = 0;
      }
      return cryptoType;
    }
    return null;
  }
}
