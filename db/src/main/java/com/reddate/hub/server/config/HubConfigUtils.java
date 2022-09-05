// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.env.Environment;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;

public class HubConfigUtils {

  private static final ConcurrentHashMap<String, HubConfigInfo> hubConfigMap =
      new ConcurrentHashMap<>();

  public static final String HUB_CONFIG_MAP = "hub";

  public static void refreshHubConfig(
      String configFileName, String privateKey, String publicKey, CryptoType cryptoType) {
    HubConfigInfo hubConfigInfo = hubConfigMap.get(HubConfigUtils.HUB_CONFIG_MAP);
    if (hubConfigInfo == null) {
      hubConfigInfo = new HubConfigInfo();
      hubConfigInfo.setConfigFileName(configFileName);
      hubConfigInfo.setPrivateKey(privateKey);
      hubConfigInfo.setPublicKey(publicKey);
      hubConfigInfo.setCryptoType(cryptoType);
      hubConfigMap.put(HubConfigUtils.HUB_CONFIG_MAP, hubConfigInfo);
    }
  }

  public static HubConfigInfo getHubConfig() {
    HubConfigInfo hubConfigInfo = hubConfigMap.get(HubConfigUtils.HUB_CONFIG_MAP);
    if (hubConfigInfo == null) {
      Environment environment = StartTaskRunner.getApplicationContext().getEnvironment();

      String privateKey = environment.getProperty("hub.privateKey");
      if (privateKey == null || privateKey.trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.HUB_PRIVATE_KEY_EMPTY.getCode(),
            ErrorMessage.HUB_PRIVATE_KEY_EMPTY.getMessage());
      }

      String publicKey = environment.getProperty("hub.publicKey");
      if (publicKey == null || publicKey.trim().isEmpty()) {
        throw new IdentityHubException(
            ErrorMessage.HUB_PULIC_KEY_EMPTY.getCode(),
            ErrorMessage.HUB_PULIC_KEY_EMPTY.getMessage());
      }
      hubConfigInfo = new HubConfigInfo();
      hubConfigInfo.setPrivateKey(privateKey);
      hubConfigInfo.setPublicKey(publicKey);
      hubConfigMap.put(HubConfigUtils.HUB_CONFIG_MAP, hubConfigInfo);
    }

    return hubConfigInfo;
  }
}
