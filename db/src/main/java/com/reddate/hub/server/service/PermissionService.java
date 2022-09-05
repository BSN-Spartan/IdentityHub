// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service;

import com.reddate.hub.hub.dto.req.AddPermissionReq;
import com.reddate.hub.hub.dto.req.DeletePermissionReq;
import com.reddate.hub.hub.dto.resp.AddPermissionResp;
import com.reddate.hub.hub.dto.resp.DeletePermissionResp;
import com.reddate.hub.hub.dto.resp.PermissionInfo;
import com.reddate.hub.hub.dto.resp.QueryGantPermissionResp;
import com.reddate.hub.hub.dto.resp.QueryPermissionResp;

public interface PermissionService {

  AddPermissionResp addPermission(String uid, AddPermissionReq resourceReq);

  QueryPermissionResp getAllPermission(String uid, String grantUid, Integer flag);

  DeletePermissionResp deletePermission(String uid, DeletePermissionReq deletePermissionReq);

  PermissionInfo getPermission(String uid, String granteeUid, String url, String grant);

  QueryGantPermissionResp getAllGrantedPermission(
      String uid, String ownerUid, String grant, Integer flag);
}
