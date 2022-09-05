// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.service;

import com.reddate.hub.hub.dto.req.RegisterHubReq;
import com.reddate.hub.hub.dto.resp.RegisterHubResp;

public interface ConnService {

  RegisterHubResp registerHub(String uid, RegisterHubReq loginReq);

  String queryPublicKey(String uid);

  String queryUidByPublicKey(String publicKey);
  
  Integer getCryptoTypeByPublicKey(String regUid, String publicKey);
}
