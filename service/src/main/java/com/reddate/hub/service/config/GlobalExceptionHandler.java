package com.reddate.hub.service.config;

import com.reddate.hub.exception.HubException;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.sdk.constant.ErrorMessage;
import com.reddate.hub.service.vo.response.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ResponseBody    
	@ExceptionHandler(Exception.class)    
	public ResponseEntity<ResultData<Object>> globalException(HttpServletResponse response,Exception ex){        
		ResultData<Object> result = new ResultData<Object>();
		
		if(ex instanceof HubException) {
			HubException HubException = (HubException)ex;
			result.setCode(HubException.getCode());
			result.setMsg(HubException.getMessage());
		}else if(ex instanceof IdentityHubException) {
			IdentityHubException identityHubException = (IdentityHubException)ex;
			result.setCode(identityHubException.getCode());
			result.setMsg(identityHubException.getMessage());
		}else {
			result.setCode(ErrorMessage.UNKNOWN_ERROR.getCode());
			result.setMsg(ErrorMessage.UNKNOWN_ERROR.getMessage() + ex.getMessage());
		}
		
		logger.info("Process failed, the error message is: " + result.getCode() + " - " + result.getMsg());
		
		return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin","*")
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
}
	
}



