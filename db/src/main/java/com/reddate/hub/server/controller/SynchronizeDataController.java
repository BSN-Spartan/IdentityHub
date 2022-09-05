// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.server.dto.GeneralAesKeyResp;
import com.reddate.hub.server.dto.SyncDataReq;
import com.reddate.hub.server.dto.SyncDataResp;
import com.reddate.hub.server.service.SynchronizeDataService;
import com.reddate.hub.server.task.SynchronizeProcessConfig;

@RestController
@RequestMapping("/synchronize")
public class SynchronizeDataController {

  @Autowired private SynchronizeDataService synchronizeDataService;

  @Autowired private SynchronizeProcessConfig synchronizeProcessConfig;

  @GetMapping("/generalAesKey")
  public ResponseEntity<GeneralAesKeyResp> generalAesKey() {
    String key = AesUtils.generalKey();
    GeneralAesKeyResp resp = new GeneralAesKeyResp();
    resp.setAesKey(key);

    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  @PostMapping("/process")
  public ResponseEntity<SyncDataResp> processSyncData(@RequestBody SyncDataReq vo) {
    SyncDataResp syncDataResp = new SyncDataResp();
    if (vo.getId() == null || vo.getId().trim().isEmpty()) {
      syncDataResp.setCode(ErrorMessage.SYNC_HUB_DATA_HUB_ID_EMPTY.getCode());
      syncDataResp.setMsg(ErrorMessage.SYNC_HUB_DATA_HUB_ID_EMPTY.getMessage());
      return new ResponseEntity<>(syncDataResp, HttpStatus.OK);
    }
    if (vo.getData() == null || vo.getData().trim().isEmpty()) {
      syncDataResp.setCode(ErrorMessage.SYNC_HUB_DATA_DATA_EMPTY.getCode());
      syncDataResp.setMsg(ErrorMessage.SYNC_HUB_DATA_DATA_EMPTY.getMessage());
      return new ResponseEntity<>(syncDataResp, HttpStatus.OK);
    }

    String aesKey = null;

    try {
      aesKey = synchronizeProcessConfig.getAesKey().get(vo.getId());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (aesKey == null || aesKey.trim().isEmpty()) {
      syncDataResp.setCode(ErrorMessage.AES_KEY_NOT_FIND.getCode());
      syncDataResp.setMsg(
          ErrorMessage.AES_KEY_NOT_FIND.getMessage() + " for the hub Id [" + vo.getId() + "]");
      return new ResponseEntity<>(syncDataResp, HttpStatus.OK);
    }

    syncDataResp = synchronizeDataService.processSyncData(vo, aesKey);

    return new ResponseEntity<>(syncDataResp, HttpStatus.OK);
  }
}
