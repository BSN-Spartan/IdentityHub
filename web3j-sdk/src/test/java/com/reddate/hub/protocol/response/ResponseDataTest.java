package com.reddate.hub.protocol.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.reddate.hub.protocol.response.ResponseData;
import org.junit.Test;

import com.reddate.hub.constant.ContractCode;
import com.reddate.hub.constant.ErrorCode;

public class ResponseDataTest {
  @Test
  public void testSetErrorCode() {
    ResponseData<Object> responseData = new ResponseData<Object>();
    responseData.setErrorCode(ErrorCode.SUCCESS);
    assertEquals("success", responseData.getErrorChMessage());
    assertEquals("success", responseData.getErrorEnMessage());
    assertEquals(0, responseData.getErrorCode().intValue());
  }

  @Test
  public void testSetErrorCode2() {
    ResponseData<Object> responseData = new ResponseData<Object>();
    responseData.setErrorCode((ErrorCode) null);
    assertEquals("success", responseData.getErrorChMessage());
    assertEquals("success", responseData.getErrorEnMessage());
    assertEquals(0, responseData.getErrorCode().intValue());
  }

  @Test
  public void testSetErrorCode3() {
    ResponseData<Object> responseData = new ResponseData<Object>();
    responseData.setErrorCode(-1);
    assertEquals(-1, responseData.getErrorCode().intValue());
  }

  @Test
  public void testSetResult() {
    ResponseData<Object> responseData = new ResponseData<Object>();
    responseData.setResult("Result");
    assertTrue(responseData.getResult() instanceof String);
  }

  @Test
  public void testConstructor() {
    ResponseData<Object> actualResponseData = new ResponseData<Object>();
    assertEquals("success", actualResponseData.getErrorChMessage());
    assertEquals("success", actualResponseData.getErrorEnMessage());
    assertEquals(0, actualResponseData.getErrorCode().intValue());
  }

  @Test
  public void testSetErrorChMessage() {
    ResponseData<Object> responseData = new ResponseData<Object>();
    responseData.setErrorChMessage("An error occurred");
    assertEquals("An error occurred", responseData.getErrorChMessage());
  }

  @Test
  public void testSetErrorEnMessage() {
    ResponseData<Object> responseData = new ResponseData<Object>();
    responseData.setErrorEnMessage("An error occurred");
    assertEquals("An error occurred", responseData.getErrorEnMessage());
  }

  @Test
  public void testConstructor2() {
    ResponseData<Object> actualResponseData = new ResponseData<Object>("Result", ErrorCode.SUCCESS);
    assertEquals("success", actualResponseData.getErrorChMessage());
    assertTrue(actualResponseData.getResult() instanceof String);
    assertEquals("success", actualResponseData.getErrorEnMessage());
    assertEquals(0, actualResponseData.getErrorCode().intValue());
  }

  @Test
  public void testConstructor4() {
    ResponseData<Object> actualResponseData =
        new ResponseData<Object>("Result", ErrorCode.SUCCESS, ContractCode.SUCCESS);
    assertEquals("success", actualResponseData.getErrorChMessage());
    assertTrue(actualResponseData.getResult() instanceof String);
    assertEquals("successsuccess", actualResponseData.getErrorEnMessage());
    assertEquals(0, actualResponseData.getErrorCode().intValue());
  }

  @Test
  public void testConstructor5() {
    ResponseData<Object> actualResponseData =
        new ResponseData<Object>("Result", null, ContractCode.SUCCESS);
    assertEquals("success", actualResponseData.getErrorChMessage());
    assertTrue(actualResponseData.getResult() instanceof String);
    assertEquals("success", actualResponseData.getErrorEnMessage());
    assertEquals(0, actualResponseData.getErrorCode().intValue());
  }

  @Test
  public void testConstructor6() {
    ResponseData<Object> actualResponseData =
        new ResponseData<Object>("Result", ErrorCode.SUCCESS, null);
    assertEquals("success", actualResponseData.getErrorChMessage());
    assertTrue(actualResponseData.getResult() instanceof String);
    assertEquals("success", actualResponseData.getErrorEnMessage());
    assertEquals(0, actualResponseData.getErrorCode().intValue());
  }
}
