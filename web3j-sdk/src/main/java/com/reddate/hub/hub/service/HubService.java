// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.service;

import com.reddate.hub.hub.config.HubConfig;

public class HubService {

  protected HubConfig hubConfig;

  public HubService(HubConfig hubConfig) {
    if (hubConfig != null) {
      this.hubConfig = hubConfig;
    }
  }

  public HubConfig getHubConfig() {
    return hubConfig;
  }

  public void setHubConfig(HubConfig hubConfig) {
    this.hubConfig = hubConfig;
  }
}
