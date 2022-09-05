package com.reddate.hub.config;

import static org.junit.Assert.assertEquals;

import com.reddate.hub.config.HubConfig;
import org.junit.Test;

public class DidConfigTest {
  @Test
  public void testSetType() {
    HubConfig hubConfig = new HubConfig();
    hubConfig.setType("Type");
    assertEquals("Type", hubConfig.getType());
  }
}
