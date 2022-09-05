// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hub.synchronize.send")
public class SynchronizeDataConfig {

  private Boolean enable;

  private String lastSyncFileName;

  private List<HostInfo> host = new ArrayList<>();

  public Boolean getEnable() {
    return enable;
  }

  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  public List<HostInfo> getHost() {
    return host;
  }

  public void setHost(List<HostInfo> host) {
    this.host = host;
  }

  public String getLastSyncFileName() {
    return lastSyncFileName;
  }

  public void setLastSyncFileName(String lastSyncFileName) {
    this.lastSyncFileName = lastSyncFileName;
  }
}
