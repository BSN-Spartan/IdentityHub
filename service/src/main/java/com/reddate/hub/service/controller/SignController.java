package com.reddate.hub.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.HubClient;
import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.constant.ErrorCode;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.utils.SignUtils;
import com.reddate.hub.service.vo.request.SignInfoReq;
import com.reddate.hub.service.vo.response.ResultData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sign")
public class SignController  extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(SignController.class);
	
	@Value("${hub.url}")
	private String hubUrl;
	
	@Value("${hub.publicKey}")
	private String hubPublicKey;
	
	@PostMapping("/verify")
	public ResponseEntity<ResultData<Boolean>> verify(@RequestBody SignInfoReq vo) {
		logger.info("path=[/hub/verify],request param = ["+JSONObject.toJSONString(vo)+"]");
		
		if(vo.getMessage() == null || vo.getMessage().trim().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"message "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), Boolean.class);
		}
		
		if(vo.getAddress() == null || vo.getAddress().trim().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"address "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), Boolean.class);
		}
		
		if(vo.getSignValue() == null || vo.getSignValue().trim().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"sign value "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), Boolean.class);
		}
		
		String publicKey = this.getPublicKey(vo.getAddress());
		
		try {
			boolean verity = SignUtils.verify(vo.getMessage(), publicKey, vo.getSignValue());
			return success(verity);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), Boolean.class);
		}
	}
	
	@PostMapping("/verifyTargetHash")
	public ResponseEntity<ResultData<Boolean>> verifyTargetHash(@RequestBody SignInfoReq vo) {
		logger.info("path=[/hub/verifyTargetHash],request param = ["+JSONObject.toJSONString(vo)+"]");
		
		if(vo.getMessage() == null || vo.getMessage().trim().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"message "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), Boolean.class);
		}
		
		if(vo.getAddress() == null || vo.getAddress().trim().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"hub/address "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), Boolean.class);
		}
		
		if(vo.getSignValue() == null || vo.getSignValue().trim().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"sign value "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), Boolean.class);
		}
				
		String publicKey = this.getPublicKey(vo.getAddress());
		
		try {
			boolean verity = SignUtils.verify2(vo.getMessage(), publicKey, vo.getSignValue());
			return success(verity);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), Boolean.class);
		}
	}
	
	private HubClient getHubClient() {
		return new HubClient(CryptoType.ECDSA,hubUrl, hubPublicKey);
	}

	public String getPublicKey(String address) {
		HubClient hubClient = getHubClient();
		String publicKey = hubClient.getPublicKeyByUid(address);
		if(publicKey == null || publicKey.isEmpty()) {
			throw new IdentityHubException(ErrorCode.PUBLIC_KEY_IS_EMPTY.getCode(),ErrorCode.PUBLIC_KEY_IS_EMPTY.getEnMessage());
		}
		return publicKey;
	}
	
}
