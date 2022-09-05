package com.reddate.hub.protocol.common;

import static org.junit.Assert.assertEquals;

import com.reddate.hub.protocol.common.KeyPair;
import org.junit.Test;

public class KeyPairTest {
  @Test
  public void testSetPrivateKey() {
    KeyPair keyPair = new KeyPair();
    keyPair.setPrivateKey("Private Key");
    assertEquals("Private Key", keyPair.getPrivateKey());
  }

  @Test
  public void testSetPublicKey() {
    KeyPair keyPair = new KeyPair();
    keyPair.setPublicKey("Public Key");
    assertEquals("Public Key", keyPair.getPublicKey());
  }

  @Test
  public void testSetType() {
    KeyPair keyPair = new KeyPair();
    keyPair.setType("Type");
    assertEquals("Type", keyPair.getType());
  }
}
