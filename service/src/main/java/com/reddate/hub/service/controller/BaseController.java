package com.reddate.hub.service.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.service.vo.response.ResultData;
import com.reddate.hub.protocol.response.Pages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	public static Integer SUCCESS_CODE = 0;
	
	public static Integer DEFAULT_ERROR_CODE = 9999;
	
	public <T> ResponseEntity<ResultData<T>> success(T data){
		if(data == null) {
			logger.debug("Process success");
		}else {
			if(logger.isDebugEnabled()) {
				logger.debug("Process success, the response data is: " + JSONObject.toJSONString(data));
			}
		}
		
		ResultData<T> response =  new ResultData<>();
		response.setCode(SUCCESS_CODE);
		response.setMsg("Success");
		response.setData(data);
		
		return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin","*")
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
	}
	
	
	public <T> ResponseEntity<ResultData<T>> error(Integer errorCode,String message,Class<T> classes){		
		ResultData<T> response =  new ResultData<>();
		if(errorCode == null) {
			response.setCode(DEFAULT_ERROR_CODE);
		}else {
			response.setCode(errorCode);
		}
		response.setMsg(message);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Process failed, the error message is: " + message);
		}
		
		return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin","*")
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
	}
	
	
	public <T> ResponseEntity<ResultData<List<T>>> listError(Integer errorCode,String message,Class<T> classes){		
		ResultData<List<T>> response =  new ResultData<>();
		if(errorCode == null) {
			response.setCode(DEFAULT_ERROR_CODE);
		}else {
			response.setCode(errorCode);
		}
		response.setMsg(message);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Process failed, the error message is: " + message);
		}
		
		return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin","*")
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
	}


	public <T> ResponseEntity<ResultData<Pages<T>>> pageError(Integer errorCode,String message, Class<T> classes){
		ResultData<Pages<T>> response =  new ResultData<>();
		if(errorCode == null) {
			response.setCode(DEFAULT_ERROR_CODE);
		}else {
			response.setCode(errorCode);
		}
		response.setMsg(message);

		if(logger.isDebugEnabled()) {
			logger.debug("Process failed, the error message is: " + message);
		}

		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin","*")
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
	}
	
}
