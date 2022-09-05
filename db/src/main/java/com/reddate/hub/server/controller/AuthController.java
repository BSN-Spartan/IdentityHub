// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.controller;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.req.AddPermissionReq;
import com.reddate.hub.hub.dto.req.CheckPermissionReq;
import com.reddate.hub.hub.dto.req.DeletePermissionReq;
import com.reddate.hub.hub.dto.req.QueryGrantedPermissionReq;
import com.reddate.hub.hub.dto.req.QueryPermissionReq;
import com.reddate.hub.hub.dto.resp.AddPermissionResp;
import com.reddate.hub.hub.dto.resp.CheckPermissionResp;
import com.reddate.hub.hub.dto.resp.DeletePermissionResp;
import com.reddate.hub.hub.dto.resp.PermissionInfo;
import com.reddate.hub.hub.dto.resp.QueryGantPermissionResp;
import com.reddate.hub.hub.dto.resp.QueryPermissionResp;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.server.service.PermissionService;
import com.reddate.hub.server.util.http.RequestParam;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired private PermissionService permissionService;

  @PostMapping("/add")
  public ResponseEntity<String> createPermission() {
    RequestParam<AddPermissionReq> requestParam = null;
    try {
      requestParam = parseRequestParam(AddPermissionReq.class);
      AddPermissionReq reqData = requestParam.getData();
      reqData.setUid(requestParam.getUid());
      AddPermissionResp addPermissionResp =
          permissionService.addPermission(requestParam.getUid(), reqData);
      return success(requestParam.getClientPublicKey(), addPermissionResp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.ADD_PERMISSION_FAILED.getCode(),
          e.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<String> deletePermission() {
    RequestParam<DeletePermissionReq> requestParam = null;
    try {
      requestParam = parseRequestParam(DeletePermissionReq.class);
      DeletePermissionResp deletePermissionResp =
          permissionService.deletePermission(requestParam.getUid(), requestParam.getData());

      return success(requestParam.getClientPublicKey(), deletePermissionResp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.DELETE_PERMISSION_FAILED.getCode(),
          ErrorMessage.DELETE_PERMISSION_FAILED.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/query")
  public ResponseEntity<String> queryPermission() {
    RequestParam<QueryPermissionReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryPermissionReq.class);

      QueryPermissionResp resp =
          permissionService.getAllPermission(
              requestParam.getUid(),
              requestParam.getData().getGrantUid(),
              Integer.parseInt(requestParam.getData().getFlag()));
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.QUERY_PERMISSION_FAILED.getCode(),
          ErrorMessage.QUERY_PERMISSION_FAILED.getMessage(),
          requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/check")
  public ResponseEntity<String> isPermission() {
    RequestParam<CheckPermissionReq> requestParam = null;
    CheckPermissionResp checkPermissionResp = new CheckPermissionResp();
    try {
      requestParam = parseRequestParam(CheckPermissionReq.class);
      CheckPermissionReq reqData = requestParam.getData();
      PermissionInfo permissionInfo =
          permissionService.getPermission(
              reqData.getOwnerUid(), reqData.getGrantUid(), reqData.getUrl(), reqData.getGrant());
      if (StringUtils.isEmpty(permissionInfo.getKey())) {
        checkPermissionResp.setSucces(false);
        checkPermissionResp.setMessage(
            ErrorMessage.PERMISSION_NOT_FOUND.getCode()
                + "-"
                + ErrorMessage.PERMISSION_NOT_FOUND.getMessage());
        return success(requestParam.getClientPublicKey(), checkPermissionResp);
      }
      checkPermissionResp.setSucces(true);
      checkPermissionResp.setKey(permissionInfo.getKey());
      checkPermissionResp.setUrl(permissionInfo.getUrl());
      return success(requestParam.getClientPublicKey(), checkPermissionResp);
    } catch (IdentityHubException e) {
      return error(
          e.getCode(),
          e.getMessage(),
          requestParam != null ? requestParam.getClientPublicKey() : null);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.UNKNOWN_ERROR.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    }
  }

  @PostMapping("/queryGrantedList")
  public ResponseEntity<String> queryGrantedPermission() {
    RequestParam<QueryGrantedPermissionReq> requestParam = null;
    try {
      requestParam = parseRequestParam(QueryGrantedPermissionReq.class);

      QueryGantPermissionResp resp =
          permissionService.getAllGrantedPermission(
              requestParam.getUid(),
              requestParam.getData().getOwnerUid(),
              requestParam.getData().getGrant(),
              Integer.parseInt(requestParam.getData().getFlag()));
      return success(requestParam.getClientPublicKey(), resp);
    } catch (IdentityHubException e) {
      return error(e.getCode(), e.getMessage(), requestParam.getClientPublicKey());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return error(
          ErrorMessage.QUERY_PERMISSION_FAILED.getCode(),
          ErrorMessage.QUERY_PERMISSION_FAILED.getMessage(),
          requestParam.getClientPublicKey());
    }
  }
}
