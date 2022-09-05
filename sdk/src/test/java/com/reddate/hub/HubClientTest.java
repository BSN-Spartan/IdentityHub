package com.reddate.hub;

import com.alibaba.fastjson.JSONObject;
import com.reddate.hub.sdk.param.CryptoType;
import com.reddate.hub.sdk.param.req.*;
import com.reddate.hub.sdk.param.resp.PermissionInfo;
import com.reddate.hub.sdk.param.resp.RegisterHubResult;
import com.reddate.hub.sdk.param.resp.SaveResourceResult;
import com.reddate.hub.sdk.protocol.common.KeyPair;
import com.reddate.hub.sdk.protocol.response.hub.CheckPermissionResp;
import com.reddate.hub.sdk.protocol.response.hub.CreatePermissionResp;
import com.reddate.hub.sdk.protocol.response.hub.DeletePermissionResp;
import com.reddate.hub.sdk.protocol.response.hub.QueryResourceResp;
import com.reddate.hub.sdk.util.ECDSAUtils;
import com.reddate.hub.sdk.util.Secp256Util;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HubClientTest extends HubClientTestBase {
	
	@Test   
	public void registerHubByPublicKeyTest() throws Exception {
		KeyPair authKeyPair = ECDSAUtils.createKey();
		
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		RegisterHubResult registerHubResult = hubClient.registerHub(authKeyPair.getPublicKey(),CryptoType.ECDSA);
		
		System.out.println(JSONObject.toJSONString(registerHubResult));
		
		assertTrue(registerHubResult.isSuccess());
	} 
	
	@Test   
	public void registerHubByIdAndPublicKeyTest() throws Exception {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(String.valueOf(System.currentTimeMillis()),keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		System.out.println(JSONObject.toJSONString(registerHubResult));
		
		assertTrue(registerHubResult.isSuccess());
	} 

	@Test   
	public void saveResourceTest2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(CryptoType.ECDSA);
		String userId = Secp256Util.getAddress(CryptoType.ECDSA, keyPair.getPrivateKey());
		RegisterHubResult registerHubResult = hubClient.registerHub(userId,  keyPair.getPublicKey(),CryptoType.ECDSA);
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(userId);
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(userId);
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		System.out.println("=================="+JSONObject.toJSONString(saveResourceResult));
		
		assertNotNull(saveResourceResult.getEncryptKey());
		assertNotNull(saveResourceResult.getUrl());
	} 

	
	@Test   
	public void saveResourceTest4() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(CryptoType.ECDSA);
		String userId = Secp256Util.getAddress(CryptoType.ECDSA, keyPair.getPrivateKey());
		RegisterHubResult registerHubResult = hubClient.registerHub(userId,keyPair.getPublicKey(),CryptoType.ECDSA);
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(CryptoType.ECDSA);
		String userId2 = Secp256Util.getAddress(CryptoType.ECDSA, keyPair2.getPrivateKey());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(userId2, keyPair2.getPublicKey(),CryptoType.ECDSA);
				
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(null);
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.WRITE);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult2.getUid());
		saveResource.setPrivateKey(keyPair2.getPrivateKey());
		saveResource.setUrl(createPermissionResp.getUrl());
		saveResource.setContent("aaaaaaaaaaaaa1111");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		System.out.println("=================="+JSONObject.toJSONString(saveResourceResult));
		
		assertNotNull(saveResourceResult.getUrl());
	} 

	
	@Test   
	public void getResourceTest2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		QueryResourceResp queryResourceResp = hubClient.getResource(registerHubResult.getUid(), keyPair.getPrivateKey(), saveResourceResult.getUrl());
		
		
		System.out.println("=================="+JSONObject.toJSONString(queryResourceResp));
		
		assertNotNull(queryResourceResp.getContent());
		assertNotNull(queryResourceResp.getKey());
	} 

	
	@Test   
	public void getResourceTest4() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()), keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		System.out.println("=================="+JSONObject.toJSONString(saveResourceResult));
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(saveResourceResult.getUrl());
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.READ);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
	
		QueryResourceResp queryResourceResp = hubClient.getResource(registerHubResult2.getUid(), keyPair2.getPrivateKey(), createPermissionResp.getUrl());
		System.out.println("=================="+JSONObject.toJSONString(queryResourceResp));
		
		assertNotNull(queryResourceResp.getContent());
		assertNotNull(queryResourceResp.getKey());
	}

	
	
	@Test   
	public void deleteResourceTest2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		Boolean delete = hubClient.deleteResource(registerHubResult.getUid(), keyPair.getPrivateKey(), saveResourceResult.getUrl());
		
		
		System.out.println("=================="+JSONObject.toJSONString(delete));
		
		assertTrue(delete);
	} 
	

	
	
	@Test   
	public void createPermissionTest2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(null);
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.WRITE);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		

		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		
		
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		assertNotNull(createPermissionResp.getUrl());
		assertNotNull(createPermissionResp.getKey());
	} 

	
	@Test   
	public void createPermissionTest4() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(saveResourceResult.getUrl());
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.READ);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		assertNotNull(createPermissionResp.getUrl());
		assertNotNull(createPermissionResp.getKey());
	} 

	
	@Test   
	public void deletePermission2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(null);
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.WRITE);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		DeletePermission deletePermission = new DeletePermission();
		deletePermission.setUid(registerHubResult.getUid());
		deletePermission.setUrl(createPermissionResp.getUrl());
		deletePermission.setPrivateKey(keyPair.getPrivateKey());
		deletePermission.setGrant(Operation.WRITE);
		deletePermission.setGrantUid(registerHubResult2.getUid());
		DeletePermissionResp deletePermissionResp = hubClient.deletePermission(deletePermission);
		
		System.out.println("=================="+JSONObject.toJSONString(deletePermissionResp));
		
		assertTrue(deletePermissionResp.isSucces());
	} 
	

	
	
	@Test   
	public void deletePermission4() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(saveResourceResult.getUrl());
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.READ);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		
		DeletePermission deletePermission = new DeletePermission();
		deletePermission.setUid(registerHubResult.getUid());
		deletePermission.setUrl(createPermissionResp.getUrl());
		deletePermission.setPrivateKey(keyPair.getPrivateKey());
		deletePermission.setGrant(Operation.READ);
		deletePermission.setGrantUid(registerHubResult2.getUid());
		DeletePermissionResp deletePermissionResp = hubClient.deletePermission(deletePermission);
		
		System.out.println("=================="+JSONObject.toJSONString(deletePermissionResp));
		
		assertTrue(deletePermissionResp.isSucces());
	} 

	
	
	@Test   
	public void queryPermission2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(saveResourceResult.getUrl());
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.READ);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		QueryPermission queryPermission = new QueryPermission();
		queryPermission.setUid(registerHubResult.getUid());

		queryPermission.setGrantUid(registerHubResult2.getUid());
		queryPermission.setPrivateKey(keyPair.getPrivateKey());
		List<PermissionInfo> permissionList = hubClient.queryPermission(queryPermission);
		
		System.out.println("=================="+JSONObject.toJSONString(permissionList));
		
		queryPermission.setFlag(UsedFlag.NO);
		List<PermissionInfo> permissionList2 = hubClient.queryPermission(queryPermission);
		System.out.println("=================="+JSONObject.toJSONString(permissionList2));
		
		assertTrue(permissionList.size() > 0);
		assertTrue(permissionList2.size() > 0);
	} 
	

	
	@Test   
	public void checkPermissionTest2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()), keyPair.getPublicKey(), hubClient.getHubCryptoType());
		
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()), keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(null);
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.WRITE);
		createPermission.setGrantUid(registerHubResult2.getUid());
		createPermission.setGrantPublicKey(keyPair2.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		
		CheckPermission checkPermission = new CheckPermission();
		checkPermission.setUid(registerHubResult2.getUid());
		checkPermission.setPrivateKey(keyPair2.getPrivateKey());
		checkPermission.setUrl(createPermissionResp.getUrl());
		checkPermission.setGrant(Operation.WRITE);
		checkPermission.setOwnerUid(registerHubResult.getUid());
		checkPermission.setGrantUid(registerHubResult2.getUid());
		
		CheckPermissionResp checkPermissionResp = hubClient.checkPermission(checkPermission);
		System.out.println("=================="+JSONObject.toJSONString(checkPermissionResp));
		
		assertTrue(checkPermissionResp.isSucces());
		assertNotNull(checkPermissionResp.getKey());
	} 
	
	@Test
	public void generalKeyPairByMnemonic() {
		List<String> mnemList = Arrays.asList("this is one test input mnemonic the size of mnemonic should 16".split(" "));
		KeyPair keyPair = com.reddate.hub.sdk.HubClient.generalKeyPairByMnemonic(mnemList);
		System.out.println("=================="+JSONObject.toJSONString(keyPair));
		assertNotNull(keyPair.getPrivateKey());
		assertNotNull(keyPair.getPublicKey());
		assertNotNull(keyPair.getType());
	}
	

	
	@Test   
	public void transferOwnerTest2() {
		com.reddate.hub.sdk.HubClient hubClient = this.getDidClient();
		
		com.reddate.hub.sdk.param.KeyPair keyPair = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair.getPrivateKey()),  keyPair.getPublicKey(), hubClient.getHubCryptoType());
		com.reddate.hub.sdk.param.KeyPair keyPair2 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult2 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair2.getPrivateKey()),  keyPair2.getPublicKey(), hubClient.getHubCryptoType());
		com.reddate.hub.sdk.param.KeyPair keyPair3 = Secp256Util.createKeyPair(hubClient.getHubCryptoType());
		RegisterHubResult registerHubResult3 = hubClient.registerHub(Secp256Util.getAddress(hubClient.getHubCryptoType(),
				keyPair3.getPrivateKey()),  keyPair3.getPublicKey(), hubClient.getHubCryptoType());
		
		SaveResource saveResource = new SaveResource();
		saveResource.setUid(registerHubResult.getUid());
		saveResource.setContent("aaaaaaaaaaaaa");
		saveResource.setGrant(Operation.WRITE);
		saveResource.setOwnerUid(registerHubResult.getUid());
		saveResource.setPrivateKey(keyPair.getPrivateKey());
		SaveResourceResult saveResourceResult = hubClient.saveResource(saveResource);
		
		
		CreatePermission createPermission = new CreatePermission();
		createPermission.setUid(registerHubResult.getUid());
		createPermission.setUrl(saveResourceResult.getUrl());
		createPermission.setPrivateKey(keyPair.getPrivateKey());
		createPermission.setGrant(Operation.READ);
		createPermission.setGrantUid(registerHubResult3.getUid());
		createPermission.setGrantPublicKey(keyPair3.getPublicKey());
		CreatePermissionResp createPermissionResp = hubClient.createPermission(createPermission);
		System.out.println("=================="+JSONObject.toJSONString(createPermissionResp));
		
		TransferOwner transferOwner = new TransferOwner();
		transferOwner.setUid(registerHubResult.getUid());
		transferOwner.setUrl(saveResourceResult.getUrl());
		transferOwner.setPrivateKey(keyPair.getPrivateKey());
		transferOwner.setNewOwnerUid(registerHubResult2.getUid());
		transferOwner.setNewOwnerPublicKey(keyPair2.getPublicKey());
		Boolean transferResult = hubClient.transferOwner(transferOwner);
		System.out.println("=================="+transferResult);
		
		QueryResourceResp queryResourceResp = hubClient.getResource(registerHubResult2.getUid(), keyPair2.getPrivateKey(), saveResourceResult.getUrl());
		String content = hubClient.decrypt(queryResourceResp.getContent(), queryResourceResp.getKey(), keyPair2.getPrivateKey());
		System.out.println(content);
		assertNotNull(content);
	} 
	
	
}
