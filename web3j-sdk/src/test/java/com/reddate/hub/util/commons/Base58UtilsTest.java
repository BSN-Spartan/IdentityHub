package com.reddate.hub.util.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;

import com.reddate.hub.util.commons.Base58Utils;
import org.junit.Assert;
import org.junit.Test;

public class Base58UtilsTest {
  @Test
  public void testEncode() {
    Assert.assertEquals("Bv49dki68Y8", Base58Utils.encode("AAAAAAAA".getBytes(StandardCharsets.UTF_8)));
    assertEquals(
        "13URjHV2c9W", Base58Utils.encode(new byte[] {0, 'A', 'A', 'A', 'A', 'A', 'A', 'A'}));
    assertEquals("", Base58Utils.encode(new byte[] {}));
  }

  @Test
  public void testDecode() throws IllegalArgumentException {
    assertThrows(IllegalArgumentException.class, () -> Base58Utils.decode("Input"));
    assertEquals(3, Base58Utils.decode("foo").length);
    assertEquals(0, Base58Utils.decode("").length);
  }

  @Test
  public void testDecode2() throws IllegalArgumentException {
    byte[] actualDecodeResult =
        Base58Utils.decode("123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz");
    assertEquals(43, actualDecodeResult.length);
    assertEquals((byte) 0, actualDecodeResult[0]);
    assertEquals((byte) 1, actualDecodeResult[1]);
    assertEquals((byte) 17, actualDecodeResult[2]);
    assertEquals((byte) -45, actualDecodeResult[3]);
    assertEquals((byte) -114, actualDecodeResult[4]);
    assertEquals((byte) 95, actualDecodeResult[5]);
    assertEquals((byte) -58, actualDecodeResult[37]);
    assertEquals((byte) 42, actualDecodeResult[38]);
    assertEquals((byte) 100, actualDecodeResult[39]);
    assertEquals((byte) 17, actualDecodeResult[40]);
    assertEquals((byte) 85, actualDecodeResult[41]);
    assertEquals((byte) -91, actualDecodeResult[42]);
  }

  @Test
  public void testDecodeToBigInteger() throws IllegalArgumentException {
    assertThrows(IllegalArgumentException.class, () -> Base58Utils.decodeToBigInteger("Input"));
    assertEquals("130546", Base58Utils.decodeToBigInteger("foo").toString());
    assertEquals(
        "584889621711786543479188656466418868554454211731455017244085522414579008191811299673190496036017573",
        Base58Utils.decodeToBigInteger("123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz")
            .toString());
    assertEquals("0", Base58Utils.decodeToBigInteger("").toString());
  }
}
