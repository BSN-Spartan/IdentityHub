package com.reddate.hub.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.HubClient;
import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.constant.ErrorCode;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.dto.resp.*;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.hub.param.req.AddPermission;
import com.reddate.hub.hub.param.req.Operation;
import com.reddate.hub.hub.param.req.UsedFlag;
import com.reddate.hub.hub.param.resp.*;
import com.reddate.hub.sdk.protocol.request.RequestParam;
import com.reddate.hub.sdk.protocol.request.hub.*;
import com.reddate.hub.sdk.protocol.response.QueryCryptoTypeResp;
import com.reddate.hub.sdk.util.Signatures;
import com.reddate.hub.service.vo.response.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/hub")
public class HubController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(HubController.class);
	
	@Value("${hub.url}")
	private String hubUrl;
	
	@Value("${hub.publicKey}")
	private String hubPublicKey;
	
	@Value("${hub.cryptoType:0}")
	private Integer hubCryptoType;

	
	@PostMapping("/regiterByIdPublicKey")
	public ResponseEntity<ResultData<RegisterHubResult>> regiterHubByIdPublickey(@RequestBody RequestParam<RegisterHubByIdPublickeyReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/regiterByPublicKey],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		if(vo.getData().getPublicKey() == null || vo.getData().getPublicKey().trim().isEmpty()) {
			return error(ErrorCode.PUBLIC_KEY_IS_EMPTY.getCode(),ErrorCode.PUBLIC_KEY_IS_EMPTY.getEnMessage(), RegisterHubResult.class);
		}
		
		if(vo.getData().getCryptoType() == null || vo.getData().getCryptoType().trim().isEmpty()) {
			return error(ErrorCode.CRYPT_TYPE_IS_EMPTY.getCode(),ErrorCode.CRYPT_TYPE_IS_EMPTY.getEnMessage(), RegisterHubResult.class);
		}
		
		CryptoType cryptoType = CryptoType.valueOf(vo.getData().getCryptoType());
		
		RegisterHubResult registerHubResult = this.getHubClient().register(vo.getData().getId(),null, vo.getData().getPublicKey(), cryptoType);
		if(registerHubResult.isSuccess()){
			registerHubResult.setUid(registerHubResult.getUid());
		}else {
			String alreadyRegisterCode = ErrorMessage.USER_RESITERD.getCode().toString();
			String message = registerHubResult.getMessage();
			if(message != null && message.contains(alreadyRegisterCode)) {
				String[] msgArr = message.split("-");
				return error(Integer.parseInt(msgArr[0].trim()),msgArr[1].trim(), RegisterHubResult.class);
			}
		}
		return success(registerHubResult);
	}
	
	@PostMapping("/saveResource")
	public ResponseEntity<ResultData<SaveResourceResp>> saveResouirce(@RequestBody RequestParam<SaveResourceReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/saveResource],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getDid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("content",vo.getData().getContent())
				.add("url",vo.getData().getUrl())
				.add("ownerUid",vo.getData().getOwnerUid())
				.add("grant",vo.getData().getGrant())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), SaveResourceResp.class);
		}
		
		Operation grant = Operation.valueOf(vo.getData().getGrant());
		String uiserPublicKey = null;
		if(vo.getDid().equals(vo.getData().getOwnerUid())) {
			uiserPublicKey = publicKey;
		}else {
			uiserPublicKey = this.getPublicKeyByUserId(vo.getData().getOwnerUid());
		}
		SaveResourceResp saveResourceResp = this.getHubClient().saveResource(vo.getData().getUid(), uiserPublicKey, uiserPublicKey,
						vo.getData().getContent(), vo.getData().getUrl(), vo.getData().getOwnerUid(),grant,vo.getData().getKey(),
						vo.getData().contractToString(),vo.getData().getSign());
		return success(saveResourceResp);
	}	
	
	
	@PostMapping("/getResource")
	public ResponseEntity<ResultData<ResourceInfo>> getResource(@RequestBody RequestParam<QueryResourceReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/getResource],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("url",vo.getData().getUrl())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), ResourceInfo.class);
		}
		
		ResourceInfo resourceInfo = this.getHubClient().getResource(vo.getData().getUid(), null,publicKey, vo.getData().getUrl(), vo.getData().contractToString(),vo.getData().getSign());
		return success(resourceInfo);
	}	
	
	@PostMapping("/deleteResource")
	public ResponseEntity<ResultData<Boolean>> deleteResource(@RequestBody RequestParam<DeleteResourceReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/deleteResource],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getDid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("url",vo.getData().getUrl())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), Boolean.class);
		}
		
		Boolean delete = this.getHubClient().deleteResource(vo.getData().getUid(), null, publicKey, vo.getData().getUrl(), vo.getData().contractToString(),vo.getData().getSign());
		return success(delete);
	}	
	
	
	@PostMapping("/createPerm")
	public ResponseEntity<ResultData<AddPermissionResult>> createPermission(@RequestBody RequestParam<CreatePermissionReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/createPerm],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		String grantPublicKey = this.getPublicKeyByUserId(vo.getData().getGrantUid());
		if(grantPublicKey == null || grantPublicKey.trim().isEmpty()) {
			return error(ErrorCode.DID_NOT_EXIST.getCode(),"grnat "+ErrorCode.DID_NOT_EXIST.getEnMessage(), AddPermissionResult.class);
		}
		if(vo.getData().getGrantPublicKey() != null) {
			if(!vo.getData().getGrantPublicKey().equals(grantPublicKey)) {
				return error(ErrorCode.GRANT_PUBLIC_KEY_WITH_DB_NOT_MATCH.getCode(), ErrorCode.GRANT_PUBLIC_KEY_WITH_DB_NOT_MATCH.getEnMessage(), AddPermissionResult.class);
			}
		}
		
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("url",vo.getData().getUrl())
				.add("grant",vo.getData().getGrant())
				.add("grantUid",vo.getData().getGrantUid())
				.add("grantPublicKey",vo.getData().getGrantPublicKey())
				.add("grantEncryptKey",vo.getData().getGrantEncryptKey())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), AddPermissionResult.class);
		}
		
		AddPermission permission = new AddPermission();
		permission.setGrant(Operation.valueOf(vo.getData().getGrant()));
		permission.setGrantPublicKey(vo.getData().getGrantPublicKey());
		permission.setGrantUid(vo.getData().getGrantUid());
		permission.setUrl(vo.getData().getUrl());
		permission.setGrantEncryptKey(vo.getData().getGrantEncryptKey());
		AddPermissionResult addPermissionResult = this.getHubClient().createPermission(vo.getData().getUid(), null, publicKey, permission, vo.getData().contractToString(),vo.getData().getSign());
		
		return success(addPermissionResult);
	}	
	
	
	
	@PostMapping("/deletePermission")
	public ResponseEntity<ResultData<DeletePermissionResult>> deletePermission(@RequestBody RequestParam<DeletePermissionReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/deletePermission],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("url",vo.getData().getUrl())
				.add("grant",vo.getData().getGrant())
				.add("grantUid",vo.getData().getGrantUid())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), DeletePermissionResult.class);
		}
		
		DeletePermissionResult deletePermissionResult = this.getHubClient().deletePermission(vo.getData().getUid(), null, publicKey, vo.getData().getUrl(), 
				vo.getData().getGrantUid(), Operation.valueOf(vo.getData().getGrant()), vo.getData().contractToString(),vo.getData().getSign());

		if(!deletePermissionResult.isSucces()) {
			String message = deletePermissionResult.getMessage();
			if(message != null && message.contains("-")) {
				String[] msgArr = message.split("-");
				return error(Integer.parseInt(msgArr[0].trim()),msgArr[1].trim(), DeletePermissionResult.class);
			}
		}
		
		return success(deletePermissionResult);
	}		
	
	
	
	@PostMapping("/queryPermission")
	public ResponseEntity<ResultData<List<PermissionInfo>>> queryPermission(@RequestBody RequestParam<QueryPermissionReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/queryPermission],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("grantUid",vo.getData().getGrantUid())
				.add("flag",vo.getData().getFlag())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return listError(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), PermissionInfo.class);
		}
		
		UsedFlag usedFlag = null;
		if(vo.getData().getFlag() != null && !vo.getData().getFlag().trim().isEmpty()) {
			usedFlag = UsedFlag.valueOf(vo.getData().getFlag());
		}
		
		List<PermissionInfo> list = this.getHubClient().queryPermission(vo.getData().getUid(), null, publicKey, 
				vo.getData().getGrantUid(),usedFlag , vo.getData().contractToString(),vo.getData().getSign());
		
		return success(list);
	}
	
	
	@PostMapping("/queryGrantedPermission")
	public ResponseEntity<ResultData<List<GrantPermissionInfo>>> queryGrantPermission(@RequestBody RequestParam<QueryGrantedPermissionReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/queryGrantedPermission],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("ownerUid",vo.getData().getOwnerUid())
				.add("grant",vo.getData().getGrant())
				.add("flag",vo.getData().getFlag())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return listError(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), GrantPermissionInfo.class);
		}
		
		UsedFlag usedFlag = null;
		if(vo.getData().getFlag() != null && !vo.getData().getFlag().trim().isEmpty()) {
			usedFlag = UsedFlag.valueOf(vo.getData().getFlag());
		}
		Operation grant = null;
		if(vo.getData().getGrant() != null && !vo.getData().getGrant().trim().isEmpty()) {
			grant = Operation.valueOf(vo.getData().getGrant());
		}
		
		List<GrantPermissionInfo> list = this.getHubClient().queryGrantedPermission(vo.getData().getUid(), null, publicKey, 
				vo.getData().getOwnerUid(),grant,usedFlag,vo.getData().contractToString(),vo.getData().getSign());
		
		return success(list);
	}

	@PostMapping("/checkPermission")
	public ResponseEntity<ResultData<CheckPermissionResp>> checkPermission(@RequestBody RequestParam<CheckPermissionReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/checkPermission],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		Operation grant = Operation.valueOf(vo.getData().getGrant());
		CheckPermissionResp chheckPermissionResp = this.getHubClient().isPermission(vo.getData().getUid(), null, publicKey,vo.getData().getOwnerUid() ,vo.getData().getGrantUid(), vo.getData().getUrl(),
				grant, vo.getData().contractToString(), vo.getData().getSign());
		return success(chheckPermissionResp);
	}

	
	@PostMapping("/getResourceHistory")
	public ResponseEntity<ResultData<List<ResourceHistoryInfo>>> queryResourceHistory(@RequestBody RequestParam<QueryResourceHistoryReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/getResourceHistory],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("url",vo.getData().getUrl())
				.add("operation",vo.getData().getOperation())
				.add("sign",vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return listError(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), ResourceHistoryInfo.class);
		}
		
		Operation operation = null;
		if(vo.getData().getOperation() != null && !vo.getData().getOperation().trim().isEmpty()) {
			operation = Operation.valueOf(vo.getData().getOperation());
		}
		
		List<ResourceHistoryInfo> list = this.getHubClient().queryResourceHistory(vo.getData().getUid(), null, publicKey, vo.getData().getUrl(), operation, vo.getData().contractToString(),vo.getData().getSign());
		
		return success(list);
	}
	
	
	@PostMapping("/transferOwner")
	public ResponseEntity<ResultData<Boolean>> transferOwner(@RequestBody RequestParam<TransferOwnerReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/transferOwner],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		String publicKey = this.getPublicKeyByUserId(vo.getData().getUid());
		
		boolean verify = Signatures.get().setInfo(vo.getProjectId(),vo.getDid())
				.add("uid", vo.getData().getUid())
				.add("url", vo.getData().getUrl())
				.add("newOwnerUid", vo.getData().getNewOwnerUid())
				.add("newOwnerPublicKey", vo.getData().getNewOwnerPublicKey())
				.add("newKey", vo.getData().getNewKey())
				.add("sign", vo.getData().getSign())
				.verify(getCryptoType(), publicKey, vo.getSign());
		if(!verify) {
			return error(ErrorCode.SIGNATURE_VERIFICATION_FAIL.getCode(),ErrorCode.SIGNATURE_VERIFICATION_FAIL.getEnMessage(), Boolean.class);
		}
		
		String newOwnerPublicKey = this.getPublicKeyByUserId(vo.getData().getNewOwnerUid());
		if(newOwnerPublicKey == null || !newOwnerPublicKey.equals(vo.getData().getNewOwnerPublicKey())) {
			return error(ErrorMessage.NEW_OWNER_PUBLIC_KEY_NOT_MATCH.getCode(),ErrorMessage.NEW_OWNER_PUBLIC_KEY_NOT_MATCH.getMessage(), Boolean.class);
		}
		
		TransferOwnerResult transferOwnerResult = this.getHubClient().transferOwner(vo.getData().getUid(), null, publicKey, vo.getData().getUrl(), vo.getData().getNewOwnerUid(), vo.getData().getNewKey(), vo.getData().contractToString(),vo.getData().getSign());
		if(!transferOwnerResult.isSucces()) {
			String message = transferOwnerResult.getMessage();
			if(message != null && message.contains("-")) {
				String[] msgArr = message.split("-");
				return error(Integer.parseInt(msgArr[0].trim()),msgArr[1].trim(), Boolean.class);
			}
		}
		
		return success(transferOwnerResult.isSucces());
	}	
	
	
	@PostMapping("/decrypt")
	public ResponseEntity<ResultData<String>> decryptHubData(@RequestBody RequestParam<DecryptHubDataReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/transferOwner],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		if(vo.getData().getCryptoType() == null || vo.getData().getCryptoType().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"cryptoType "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), String.class);
		}
		if(vo.getData().getContent() == null || vo.getData().getContent().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"content "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), String.class);
		}
		if(vo.getData().getEncptyKey() == null || vo.getData().getEncptyKey().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"encptyKey "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), String.class);
		}
		if(vo.getData().getPrivateKey() == null || vo.getData().getPrivateKey().isEmpty()) {
			return error(ErrorCode.PARAMETER_IS_EMPTY.getCode(),"privateKey "+ErrorCode.PARAMETER_IS_EMPTY.getEnMessage(), String.class);
		}
		
		CryptoType cryptoType =  null;
		try {
			cryptoType = CryptoType.valueOf(vo.getData().getCryptoType());
		} catch (Exception e) {
			return error(ErrorMessage.DECRYPT_FAILED.getCode(),ErrorMessage.DECRYPT_FAILED.getMessage(), String.class);
		}
		
		String plainText = HubClient.decrypt(cryptoType, vo.getData().getContent(), vo.getData().getEncptyKey(), vo.getData().getPrivateKey());
		
		return success(plainText);
	}
	
	
	@PostMapping("/getType")
	public ResponseEntity<ResultData<QueryCryptoTypeResp>> getCryptoTypeByIdOrPublicKey(@RequestBody RequestParam<QueryCryptoTypeReq> vo){
		if(logger.isDebugEnabled()) {
			logger.debug("path=[/hub/getType],request param = ["+JSONObject.toJSONString(vo)+"]");
		}
		
		Integer cryptoType = this.getHubClient().getCryptoTypeByPublicKey(vo.getData().getUid(), vo.getData().getPublicKey());
		QueryCryptoTypeResp resp = new QueryCryptoTypeResp();
		resp.setType(cryptoType);
		
		return success(resp);
	}
	
	
	private HubClient getHubClient() {
		return new HubClient(CryptoType.ofVlaue(hubCryptoType),hubUrl, hubPublicKey);
	}
	
	private com.reddate.hub.sdk.param.CryptoType getCryptoType() {
		if(hubCryptoType == null) {
			throw new IdentityHubException(ErrorMessage.SERVICE_CRYPTO_TYPE_EMPTY.getCode(),ErrorMessage.SERVICE_CRYPTO_TYPE_EMPTY.getMessage());
		}
		return com.reddate.hub.sdk.param.CryptoType.ofVlaue(hubCryptoType);
	}
	
	private String getPublicKeyByUserId(String uid) {
		String publicKey = this.getHubClient().getPublicKeyByUid(uid);
		if(publicKey == null || publicKey.trim().isEmpty()) {
			throw new IdentityHubException(ErrorMessage.USER_NOT_EXISTS.getCode(),ErrorMessage.USER_NOT_EXISTS.getMessage());
		}
		return publicKey;
	}
	
}
