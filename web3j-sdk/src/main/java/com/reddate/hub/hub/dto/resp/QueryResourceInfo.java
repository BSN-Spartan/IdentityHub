// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.dto.resp;

import java.io.Serializable;
import java.util.List;

import com.reddate.hub.hub.dto.HubData;

public class QueryResourceInfo extends HubData implements Serializable {

  private List<QueryResourceResp> queryResourceResps;

  public List<QueryResourceResp> getQueryResourceResps() {
    return queryResourceResps;
  }

  public void setQueryResourceResps(List<QueryResourceResp> queryResourceResps) {
    this.queryResourceResps = queryResourceResps;
  }

  @Override
  public String seriToString() {
    return super.toJsonString(this);
  }

  @Override
  public Object paseFromSeriString(String data) {
    return super.paseFromJsonString(data, QueryResourceInfo.class);
  }
}
