// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.controller;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.DeleteResourceReq;
import com.reddate.hub.hub.dto.req.GetUidInfoReq;
import com.reddate.hub.hub.dto.req.QueryResourceHistoryReq;
import com.reddate.hub.hub.dto.req.QueryResourceReq;
import com.reddate.hub.hub.dto.req.ResourceKeyInfo;
import com.reddate.hub.hub.dto.req.SaveResourceReq;
import com.reddate.hub.hub.dto.req.TransferOwnerReq;
import com.reddate.hub.hub.dto.resp.DeleteResourceResp;
import com.reddate.hub.hub.dto.resp.QueryResourceHistoryResp;
import com.reddate.hub.hub.dto.resp.QueryResourceInfo;
import com.reddate.hub.hub.dto.resp.QueryResourceResp;
import com.reddate.hub.hub.dto.resp.SaveResourceResp;
import com.reddate.hub.hub.dto.resp.TransferOwnerResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.server.service.ResourceService;
import com.reddate.hub.server.util.http.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

  @Autowired private ResourceService resourceService;

  @PostMapping("/save")
  public ResponseEntity<String> saveResource() {
    RequestParam<SaveResourceReq> requestParam = null;
    try {
      requestParam = parseRequestParam(SaveResourceReq.class);
      SaveResourceReq resourceReq = requestParam.getData();
      SaveResourceResp saveResourceResp =
          resourceService.saveResource(requestParam.getUid(), resourceReq);
      return success(requestParam.getClientPublicKey(), saveResourceResp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.SAVE_RESOURCE_ERROR.getCode(),
          ErrorMessage.SAVE_RESOURCE_ERROR.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/query")
  public ResponseEntity<String> getResource() {
    RequestParam<QueryResourceReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryResourceReq.class);
      QueryResourceResp resp =
          resourceService.getResource(requestParam.getUid(), requestParam.getData());
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.QUERY_RESOURCE_ERROR.getCode(),
          ErrorMessage.QUERY_RESOURCE_ERROR.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<String> deleteResource() {
    RequestParam<DeleteResourceReq> requestParam = null;
    try {
      requestParam = parseRequestParam(DeleteResourceReq.class);
      DeleteResourceResp resp =
          resourceService.deleteResource(requestParam.getUid(), requestParam.getData());
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    }
  }

 /* @PostMapping("/update")
  public ResponseEntity<String> findResource() {
    RequestParam<GetUidInfoReq> requestParam = null;
    try {
      requestParam = parseRequestParam(GetUidInfoReq.class);
      List<ResourceKeyInfo> resourceKeyInfos = requestParam.getData().getResourceKeyList();
      QueryResourceInfo queryResourceInfo = new QueryResourceInfo();
      for (ResourceKeyInfo resourceKeyInfo : resourceKeyInfos) {
        resourceService.updateResource(resourceKeyInfo.getUrl(), resourceKeyInfo.getKey());
      }
      return success(requestParam.getClientPublicKey(), queryResourceInfo);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.UPDATE_RESOURCE_ERROR.getCode(),
          ErrorMessage.UPDATE_RESOURCE_ERROR.getMessage(),
          requestParam.getClientPublicKey());
    }
  }*/

  @PostMapping("/queryHistory")
  public ResponseEntity<String> queryResourceHistory() {
    RequestParam<QueryResourceHistoryReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryResourceHistoryReq.class);
      QueryResourceHistoryResp resp =
          resourceService.queryResourceHistory(requestParam.getUid(), requestParam.getData());
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.QUERY_RESOURCE_HISTORY_ERROR.getCode(),
          ErrorMessage.QUERY_RESOURCE_HISTORY_ERROR.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/transferOwner")
  public ResponseEntity<String> transferOwner() {
    RequestParam<TransferOwnerReq> requestParam = null;
    try {
      requestParam = parseRequestParam(TransferOwnerReq.class);
      TransferOwnerResp resp =
          resourceService.transferOwner(requestParam.getUid(), requestParam.getData());
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    }
  }
}
