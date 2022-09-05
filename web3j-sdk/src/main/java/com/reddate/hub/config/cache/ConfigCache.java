// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.config.cache;

import com.reddate.hub.config.ContractAddress;
import com.reddate.hub.config.HubConfig;

import java.util.concurrent.ConcurrentHashMap;

public class ConfigCache {

  public static final String CONFIG_KEY = "didConfigInfo";

  public static final String CONTRACT_ADDRESS = "contractAddress";

  private static final ConcurrentHashMap<String, HubConfig> configInfo = new ConcurrentHashMap<>();

  private static final ConcurrentHashMap<String, ContractAddress> addressInfo =
      new ConcurrentHashMap<>();

  public static void putConfig(HubConfig config) {
    configInfo.put(CONFIG_KEY, config);
  }

  public static HubConfig getConfig() {
    return configInfo.get(CONFIG_KEY);
  }

  public static void putAddress(ContractAddress config) {
    addressInfo.put(CONTRACT_ADDRESS, config);
  }

  public static ContractAddress getAddress() {

    return addressInfo.get(CONTRACT_ADDRESS);
  }
}
