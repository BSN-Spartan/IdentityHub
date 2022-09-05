package com.reddate.hub.util.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.reddate.hub.util.commons.RipeMDUtils;
import org.junit.Test;

public class RipeMDUtilsTest {
  @Test
  public void testEncodeRipeMD128() throws Exception {
    RipeMDUtils ripeMDUtils = new RipeMDUtils();
    byte[] actualEncodeRipeMD128Result = RipeMDUtils.encodeRipeMD128("AAAAAAAA".getBytes());
    assertEquals(Short.SIZE, actualEncodeRipeMD128Result.length);
    assertEquals((byte) 119, actualEncodeRipeMD128Result[0]);
    assertEquals((byte) 86, actualEncodeRipeMD128Result[1]);
    assertEquals((byte) -43, actualEncodeRipeMD128Result[2]);
    assertEquals((byte) 12, actualEncodeRipeMD128Result[3]);
    assertEquals((byte) -106, actualEncodeRipeMD128Result[4]);
    assertEquals((byte) 96, actualEncodeRipeMD128Result[5]);
    assertEquals((byte) -40, actualEncodeRipeMD128Result[10]);
    assertEquals((byte) -25, actualEncodeRipeMD128Result[11]);
    assertEquals((byte) -116, actualEncodeRipeMD128Result[12]);
    assertEquals((byte) -116, actualEncodeRipeMD128Result[13]);
    assertEquals((byte) 49, actualEncodeRipeMD128Result[14]);
    assertEquals((byte) 119, actualEncodeRipeMD128Result[15]);
  }

  @Test
  public void testEncodeRipeMD128Hex() throws Exception {
    assertEquals(
        "7756d50c966032e4165cd8e78c8c3177", RipeMDUtils.encodeRipeMD128Hex("AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeRipeMD160Hex() throws Exception {
    assertEquals(
        "c7b8d405045aeb275b9585469870fe3cf39f4119",
        RipeMDUtils.encodeRipeMD160Hex("AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeRipeMD256() throws Exception {
    byte[] actualEncodeRipeMD256Result = RipeMDUtils.encodeRipeMD256("AAAAAAAA".getBytes());
    assertEquals(Integer.SIZE, actualEncodeRipeMD256Result.length);
    assertEquals(Byte.MAX_VALUE, actualEncodeRipeMD256Result[0]);
    assertEquals((byte) -125, actualEncodeRipeMD256Result[1]);
    assertEquals((byte) -68, actualEncodeRipeMD256Result[2]);
    assertEquals((byte) 34, actualEncodeRipeMD256Result[3]);
    assertEquals((byte) 35, actualEncodeRipeMD256Result[4]);
    assertEquals((byte) 108, actualEncodeRipeMD256Result[5]);
    assertEquals((byte) -124, actualEncodeRipeMD256Result[26]);
    assertEquals((byte) 43, actualEncodeRipeMD256Result[27]);
    assertEquals((byte) 25, actualEncodeRipeMD256Result[28]);
    assertEquals((byte) -8, actualEncodeRipeMD256Result[29]);
    assertEquals((byte) 104, actualEncodeRipeMD256Result[30]);
    assertEquals((byte) -117, actualEncodeRipeMD256Result[31]);
  }

  @Test
  public void testEncodeRipeMD256Hex() throws Exception {
    assertEquals(
        "7f83bc22236c040568da5449807ebb68c568228d3d2f4948b35b842b19f8688b",
        RipeMDUtils.encodeRipeMD256Hex("AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeRipeMD320() throws Exception {
    byte[] actualEncodeRipeMD320Result = RipeMDUtils.encodeRipeMD320("AAAAAAAA".getBytes());
    assertEquals((byte) 40, actualEncodeRipeMD320Result.length);
    assertEquals((byte) -106, actualEncodeRipeMD320Result[0]);
    assertEquals((byte) -94, actualEncodeRipeMD320Result[1]);
    assertEquals((byte) 78, actualEncodeRipeMD320Result[2]);
    assertEquals((byte) 54, actualEncodeRipeMD320Result[3]);
    assertEquals((byte) 65, actualEncodeRipeMD320Result[4]);
    assertEquals((byte) 74, actualEncodeRipeMD320Result[5]);
    assertEquals((byte) -122, actualEncodeRipeMD320Result[34]);
    assertEquals((byte) -73, actualEncodeRipeMD320Result[35]);
    assertEquals((byte) 55, actualEncodeRipeMD320Result[36]);
    assertEquals((byte) -43, actualEncodeRipeMD320Result[37]);
    assertEquals((byte) 120, actualEncodeRipeMD320Result[38]);
    assertEquals((byte) 54, actualEncodeRipeMD320Result[39]);
  }

  @Test
  public void testEncodeRipeMD320Hex() throws Exception {
    assertEquals(
        "96a24e36414aa3c2b6ee9e018369b34ff38438163fca290674cc003889c19a48c0a286b737d57836",
        RipeMDUtils.encodeRipeMD320Hex("AAAAAAAA".getBytes()));
  }

  @Test
  public void testInitHmacRipeMD128Key() throws Exception {
    assertEquals(Short.SIZE, RipeMDUtils.initHmacRipeMD128Key().length);
  }

  @Test
  public void testEncodeHmacRipeMD128() throws Exception {
    byte[] data = "AAAAAAAA".getBytes();
    byte[] actualEncodeHmacRipeMD128Result =
        RipeMDUtils.encodeHmacRipeMD128(data, "AAAAAAAA".getBytes());
    assertEquals(Short.SIZE, actualEncodeHmacRipeMD128Result.length);
    assertEquals((byte) -60, actualEncodeHmacRipeMD128Result[0]);
    assertEquals((byte) 15, actualEncodeHmacRipeMD128Result[1]);
    assertEquals((byte) -13, actualEncodeHmacRipeMD128Result[2]);
    assertEquals((byte) -102, actualEncodeHmacRipeMD128Result[3]);
    assertEquals((byte) 70, actualEncodeHmacRipeMD128Result[4]);
    assertEquals((byte) -76, actualEncodeHmacRipeMD128Result[5]);
    assertEquals((byte) 32, actualEncodeHmacRipeMD128Result[10]);
    assertEquals((byte) 38, actualEncodeHmacRipeMD128Result[11]);
    assertEquals((byte) 118, actualEncodeHmacRipeMD128Result[12]);
    assertEquals((byte) 113, actualEncodeHmacRipeMD128Result[13]);
    assertEquals((byte) 94, actualEncodeHmacRipeMD128Result[14]);
    assertEquals((byte) -109, actualEncodeHmacRipeMD128Result[15]);
  }

  @Test
  public void testEncodeHmacRipeMD1282() throws Exception {
    assertThrows(Exception.class, () -> RipeMDUtils.encodeHmacRipeMD128(null, null));
  }

  @Test
  public void testEncodeHmacRipeMD1283() throws Exception {
    assertThrows(
        Exception.class,
        () -> RipeMDUtils.encodeHmacRipeMD128(new byte[] {}, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD1284() throws Exception {
    assertThrows(
        Exception.class, () -> RipeMDUtils.encodeHmacRipeMD128("AAAAAAAA".getBytes(), null));
  }

  @Test
  public void testEncodeHmacRipeMD1285() throws Exception {
    assertThrows(
        Exception.class,
        () -> RipeMDUtils.encodeHmacRipeMD128("AAAAAAAA".getBytes(), new byte[] {}));
  }

  @Test
  public void testEncodeHmacRipeMD128Hex() throws Exception {
    byte[] data = "AAAAAAAA".getBytes();
    assertEquals(
        "c40ff39a46b4fcaec9c4202676715e93",
        RipeMDUtils.encodeHmacRipeMD128Hex(data, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD128Hex2() throws Exception {
    assertThrows(Exception.class, () -> RipeMDUtils.encodeHmacRipeMD128Hex(null, null));
  }

  @Test
  public void testEncodeHmacRipeMD128Hex3() throws Exception {
    assertThrows(
        Exception.class, () -> RipeMDUtils.encodeHmacRipeMD128Hex(null, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD128Hex4() throws Exception {
    assertThrows(
        Exception.class,
        () -> RipeMDUtils.encodeHmacRipeMD128Hex(new byte[] {}, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD128Hex5() throws Exception {
    assertThrows(
        Exception.class,
        () -> RipeMDUtils.encodeHmacRipeMD128Hex("AAAAAAAA".getBytes(), new byte[] {}));
  }

  @Test
  public void testInitHmacRipeMD160Key() throws Exception {
    assertEquals(20, RipeMDUtils.initHmacRipeMD160Key().length);
  }

  @Test
  public void testEncodeHmacRipeMD160() throws Exception {
    assertNull(
        RipeMDUtils.encodeHmacRipeMD160(new byte[] {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'}, null));
    assertNull(RipeMDUtils.encodeHmacRipeMD160("AAAAAAAA".getBytes(), new byte[] {}));
  }

  @Test
  public void testEncodeHmacRipeMD160Hex() throws Exception {
    byte[] data = "AAAAAAAA".getBytes();
    assertEquals(
        "899800e4da1c707f6454a77d0fb473cbabf5915d",
        RipeMDUtils.encodeHmacRipeMD160Hex(data, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD160Hex2() throws Exception {
    assertNull(RipeMDUtils.encodeHmacRipeMD160Hex(null, null));
  }

  @Test
  public void testEncodeHmacRipeMD160Hex3() throws Exception {
    assertNull(RipeMDUtils.encodeHmacRipeMD160Hex(null, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD160Hex4() throws Exception {
    assertNull(RipeMDUtils.encodeHmacRipeMD160Hex(new byte[] {}, "AAAAAAAA".getBytes()));
  }

  @Test
  public void testEncodeHmacRipeMD160Hex5() throws Exception {
    assertNull(RipeMDUtils.encodeHmacRipeMD160Hex("AAAAAAAA".getBytes(), new byte[] {}));
  }
}
