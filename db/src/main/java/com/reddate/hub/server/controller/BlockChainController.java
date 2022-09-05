// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.AddBlockChainReq;
import com.reddate.hub.hub.dto.req.QueryBlockChainInfoReq;
import com.reddate.hub.hub.dto.resp.AddBlockChainResp;
import com.reddate.hub.hub.dto.resp.QueryBlockChainInfoResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.server.service.BlockChainService;
import com.reddate.hub.server.util.http.RequestParam;


@RestController
@RequestMapping("/chain")
public class BlockChainController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(BlockChainController.class);

  @Autowired private BlockChainService blockChainService;

  @PostMapping("/register")
  public ResponseEntity<String> addChainInfo() {
    RequestParam<AddBlockChainReq> requestParam = null;
    try {
      requestParam = parseRequestParam(AddBlockChainReq.class);
      AddBlockChainReq reqData = requestParam.getData();

      String addResult = blockChainService.addBlockChainInfo(reqData);
      AddBlockChainResp addBlockChainResp = new AddBlockChainResp();
      if (addResult == null) {
        addBlockChainResp.setRestult(true);
      } else {
        addBlockChainResp.setRestult(false);
        addBlockChainResp.setMessage(addResult);
      }
      return success(requestParam.getUserPublicKey(), addBlockChainResp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getUserPublicKey());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage(), requestParam.getUserPublicKey());
    }
  }

  @PostMapping("/query")
  public ResponseEntity<String> queryChainInfo() {
    RequestParam<QueryBlockChainInfoReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryBlockChainInfoReq.class);
      QueryBlockChainInfoReq reqData = requestParam.getData();
      QueryBlockChainInfoResp queryBlockChainInfoResp =
          blockChainService.queryBlockChainInfo(reqData.getAddress());

      return success(requestParam.getUserPublicKey(), queryBlockChainInfoResp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getUserPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage(), requestParam.getUserPublicKey());
    }
  }
}
