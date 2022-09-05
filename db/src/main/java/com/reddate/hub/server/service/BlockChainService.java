// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service;

import com.reddate.hub.hub.dto.req.AddBlockChainReq;
import com.reddate.hub.hub.dto.resp.QueryBlockChainInfoResp;

public interface BlockChainService {

  String addBlockChainInfo(AddBlockChainReq req);

  QueryBlockChainInfoResp queryBlockChainInfo(String address);
}
