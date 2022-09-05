package com.reddate.hub.constant;

import static org.junit.Assert.assertEquals;

import com.reddate.hub.config.cache.HubConfigConstant;
import com.reddate.hub.constant.CurrencyCode;
import com.reddate.hub.constant.ErrorCode;
import org.junit.Assert;
import org.junit.Test;

public class ErrorCodeTest {
  @Test
  public void testGetTypeByErrorCode() {
    new HubConfigConstant();
    new CurrencyCode();
    Assert.assertEquals(ErrorCode.UNKNOWN_ERROR, ErrorCode.getTypeByErrorCode(-1));
    assertEquals(ErrorCode.UNKNOWN_ERROR, ErrorCode.getTypeByErrorCode(1));
    assertEquals(ErrorCode.SUCCESS, ErrorCode.getTypeByErrorCode(0));
  }

  @Test
  public void testSetChMessage() {
  }

  @Test
  public void testSetCode() {
    ErrorCode.SUCCESS.setCode(1);
  }

  @Test
  public void testSetEnMessage() {
    ErrorCode.SUCCESS.setEnMessage("En Message");
  }
}
