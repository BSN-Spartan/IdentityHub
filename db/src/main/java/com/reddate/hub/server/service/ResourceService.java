// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service;

import java.util.List;

import com.reddate.hub.hub.dto.req.DeleteResourceReq;
import com.reddate.hub.hub.dto.req.QueryResourceHistoryReq;
import com.reddate.hub.hub.dto.req.QueryResourceReq;
import com.reddate.hub.hub.dto.req.SaveResourceReq;
import com.reddate.hub.hub.dto.req.TransferOwnerReq;
import com.reddate.hub.hub.dto.resp.DeleteResourceResp;
import com.reddate.hub.hub.dto.resp.QueryResourceHistoryResp;
import com.reddate.hub.hub.dto.resp.QueryResourceResp;
import com.reddate.hub.hub.dto.resp.SaveResourceResp;
import com.reddate.hub.hub.dto.resp.TransferOwnerResp;

public interface ResourceService {

  SaveResourceResp saveResource(String uid, SaveResourceReq resourceReq);

  QueryResourceResp getResource(String uid, QueryResourceReq queryResourceReq);

  DeleteResourceResp deleteResource(String uid, DeleteResourceReq deleteResourceReq);

  List<QueryResourceResp> queryResource(String uid);

  void updateResource(String uid, String key);

  QueryResourceHistoryResp queryResourceHistory(
      String uid, QueryResourceHistoryReq queryResourceHisReq);

  TransferOwnerResp transferOwner(String uid, TransferOwnerReq transferOwnerReq);
}
