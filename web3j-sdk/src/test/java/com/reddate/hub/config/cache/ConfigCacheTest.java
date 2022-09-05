package com.reddate.hub.config.cache;

import static org.junit.Assert.assertNull;

import com.reddate.hub.config.HubConfig;
import com.reddate.hub.config.cache.ConfigCache;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ConfigCacheTest {

  public static final String CONFIG_KEY = "didConfigINfo";

  private static final Map<String, HubConfig> configInfo = new HashMap<String, HubConfig>();

  @Test
  public void testPutConfig() {
    ConfigCache configCache = new ConfigCache();
  }

  @Test
  public void testGetConfig() {
    ConfigCache.getConfig();
  }
}
