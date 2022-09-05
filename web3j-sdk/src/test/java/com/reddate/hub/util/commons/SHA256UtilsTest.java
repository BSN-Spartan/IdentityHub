package com.reddate.hub.util.commons;

import static org.junit.Assert.assertEquals;

import com.reddate.hub.util.commons.SHA256Utils;
import org.junit.Test;

public class SHA256UtilsTest {
  @Test
  public void testGetSHA256() {
    byte[] actualSHA256 = SHA256Utils.getSHA256("Str");
    assertEquals(Integer.SIZE, actualSHA256.length);
    assertEquals((byte) -128, actualSHA256[0]);
    assertEquals((byte) -124, actualSHA256[1]);
    assertEquals((byte) -91, actualSHA256[2]);
    assertEquals((byte) 27, actualSHA256[3]);
    assertEquals((byte) 60, actualSHA256[4]);
    assertEquals((byte) 100, actualSHA256[5]);
    assertEquals((byte) 91, actualSHA256[26]);
    assertEquals((byte) 24, actualSHA256[27]);
    assertEquals((byte) -112, actualSHA256[28]);
    assertEquals((byte) -128, actualSHA256[29]);
    assertEquals((byte) 123, actualSHA256[30]);
    assertEquals((byte) 70, actualSHA256[31]);
  }

  @Test
  public void testGetSHA256String() {
    assertEquals(
        "8084a51b3c649e8899252edf603c110c821c00176fad58c411925b1890807b46",
        SHA256Utils.getSHA256String("Str"));
  }
}
