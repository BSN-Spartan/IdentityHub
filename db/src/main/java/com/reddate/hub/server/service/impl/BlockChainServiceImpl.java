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
import com.reddate.hub.hub.dto.req.AddBlockChainReq;
import com.reddate.hub.hub.dto.resp.QueryBlockChainInfoResp;
import com.reddate.hub.server.constant.HubConstat;
import com.reddate.hub.server.service.BlockChainService;

@Service
public class BlockChainServiceImpl implements BlockChainService {

  public static final String CHAIN_ACCOUNT_COLLLECT = "blockChainAccount";

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public String addBlockChainInfo(AddBlockChainReq req) {

    QueryBlockChainInfoResp query = queryBlockChainInfo(req.getAddress());
    if (query != null) {
      if (query.getPublicKey().equals(req.getPublicKey())
          && query.getChainType().equals(req.getChainType())) {
        return null;
      } else {
        return ErrorMessage.ADDR_HAS_EXIST.getCode()
            + "-"
            + ErrorMessage.ADDR_HAS_EXIST.getMessage();
      }
    }

    Map<String, Object> map = new HashMap<>();
    map.put("addrId", req.getAddrId());
    map.put("address", req.getAddress());
    map.put("publicKey", req.getPublicKey());
    map.put("chainType", req.getChainType());
    map.put("createTime", LocalDateTime.now().toString());
    map.put(HubConstat.LAST_UPDATE_TIME, LocalDateTime.now());

    Map<String, Object> saverst = mongoTemplate.save(map, CHAIN_ACCOUNT_COLLLECT);
    if (saverst == null) {
      return ErrorMessage.ADD_ADDR_PUB_FAILED.getCode()
          + "-"
          + ErrorMessage.ADD_ADDR_PUB_FAILED.getMessage();
    }

    return null;
  }

  @Override
  public QueryBlockChainInfoResp queryBlockChainInfo(String address) {
    Criteria c1 = Criteria.where("addrId").is(address);
    Criteria c2 = Criteria.where("address").is(address);
    Criteria cr = new Criteria();
    Query query = new Query(cr.orOperator(c1, c2));

    Map resourceMap = mongoTemplate.findOne(query, Map.class, CHAIN_ACCOUNT_COLLLECT);
    if (resourceMap == null) {
      return null;
    }
    String chainAddress = (String) resourceMap.get("address");
    if (chainAddress == null || chainAddress.trim().isEmpty()) {
      return null;
    }

    String chainAddrId = (String) resourceMap.get("addrId");
    String chainPublicKey = (String) resourceMap.get("publicKey");
    String chainType = (String) resourceMap.get("chainType");
    QueryBlockChainInfoResp info = new QueryBlockChainInfoResp();
    info.setAddrId(chainAddrId);
    info.setAddress(chainAddress);
    info.setPublicKey(chainPublicKey);
    info.setChainType(chainType);
    return info;
  }
}
