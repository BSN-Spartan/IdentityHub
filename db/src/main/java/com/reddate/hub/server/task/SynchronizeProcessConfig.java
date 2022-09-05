// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hub.synchronize.receive")
public class SynchronizeProcessConfig {

  private Map<String, String> aesKey = new HashMap<>();

  public Map<String, String> getAesKey() {
    return aesKey;
  }

  public void setAesKey(Map<String, String> aesKey) {
    this.aesKey = aesKey;
  }
}
