package com.reddate.hub.constant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.reddate.hub.constant.ContractCode;
import org.junit.Assert;
import org.junit.Test;

public class ContractCodeTest {
  @Test
  public void testGetByCode() {
    Assert.assertEquals(ContractCode.UNKNOWN_ERROR, ContractCode.getByCode("An error occurred"));
    assertEquals(ContractCode.UNKNOWN_ERROR, ContractCode.getByCode("foo"));
    assertEquals(ContractCode.SUCCESS, ContractCode.getByCode("0000"));
  }

  @Test
  public void testGetEnMessage() {
    assertEquals("success", ContractCode.getEnMessage(ContractCode.SUCCESS));
    assertEquals("Unknow Exception", ContractCode.getEnMessage(ContractCode.UNKNOWN_ERROR));
  }

  @Test
  public void testIsSuccess() {
    assertTrue(ContractCode.isSuccess(ContractCode.SUCCESS));
    assertFalse(ContractCode.isSuccess(ContractCode.UNKNOWN_ERROR));
    assertFalse(ContractCode.isSuccess("Code"));
    assertTrue(ContractCode.isSuccess("0000"));
  }
}
