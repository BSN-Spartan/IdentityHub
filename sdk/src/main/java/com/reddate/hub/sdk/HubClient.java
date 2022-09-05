package com.reddate.hub.sdk;

import com.reddate.hub.sdk.constant.ErrorMessage;
import com.reddate.hub.sdk.exception.HubException;
import com.reddate.hub.sdk.param.CryptoType;
import com.reddate.hub.sdk.param.req.*;
import com.reddate.hub.sdk.param.resp.*;
import com.reddate.hub.sdk.protocol.common.KeyPair;
import com.reddate.hub.sdk.protocol.response.hub.CheckPermissionResp;
import com.reddate.hub.sdk.protocol.response.hub.CreatePermissionResp;
import com.reddate.hub.sdk.protocol.response.hub.DeletePermissionResp;
import com.reddate.hub.sdk.protocol.response.hub.QueryResourceResp;
import com.reddate.hub.sdk.service.HubService;

import java.util.List;

/**
 * Did SDK main class, all the hub service can be called by this class method.
 * 
 * Before call hub service, you need create hub client instance.
 */
public class HubClient {

	/**
	 * Identify HUB module service logic implement class
	 * 
	 */
	private HubService hubService;


	/**
	 * Did client construct for connect to the hub service
	 *
	 * @param url       hub service URL
	 */
	public HubClient(String url) {
		this(url, "", "", CryptoType.ECDSA);
	}

	/**
	 * Did client construct for connect to the hub service
	 * 
	 * @param url       hub service URL
	 * @param projectId The project Id of assign
	 * @param token     The Token of assign
	 */
	public HubClient(String url, String projectId, String token) {
		this(url, projectId, token,CryptoType.ECDSA);
	}
	
	/**
	 * Did client construct for connect to the self-deployed hub service
	 * 
	 * @param url       hub service URL
	 * @param projectId The project Id 
	 * @param token     The Token 
	 * @param hubCryptoType hub's crypto type
	 */
	public HubClient(String url, String projectId, String token, CryptoType hubCryptoType) {

		hubService = new HubService(url, projectId, token, hubCryptoType);
	}
	
	/**
	 * Register a identify hub user by the public key.
	 * 
	 * @param publicKey decimal public key String
	 * @param cryptoType encryption Algorithm 
	 * @return Return the register result
	 */
	public RegisterHubResult registerHub(String publicKey,CryptoType cryptoType) {
		return registerHub(null, publicKey,cryptoType);
	}
	
	
	/**
	 * Register a identify hub user by the public key.
	 * 
	 * @param id  hub user id
	 * @param publicKey decimal public key String
	 * @param cryptoType encryption Algorithm 
	 * @return Return the register result
	 */
	public RegisterHubResult registerHub(String id, String publicKey,CryptoType cryptoType) {
		try {
			return hubService.registerHubByIdPublicKey(id, publicKey, cryptoType);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Save one resource to the identify hub, this function validate the user
	 * permission first, then save the resource to the identify hub if have
	 * permission
	 * 
	 * @param saveResource Save resource detail information
	 * @return Return the saved resource and encrypt Key
	 */
	public SaveResourceResult saveResource(SaveResource saveResource) {
		try {
			return hubService.saveResource(saveResource);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Query the saved resource in the identify hub, return the saved resource
	 * information
	 * 
	 * @param uid        user id
	 * @param privateKey The identify hub user's private key
	 * @param url        The resource URL in identify hub
	 * @return Return the resource encrypt content and encrypt Key
	 */
	public QueryResourceResp getResource(String uid, String privateKey, String url) {
		try {
			return hubService.getResource(uid, privateKey, url);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Delete the resource in the identify hub, this function validate the identify
	 * hub user's permission first, if this user have permission, then delete this
	 * permission.
	 * 
	 * @param uid        user id
	 * @param privateKey The identify hub user's private key
	 * @param url        The resource user in identify hub
	 * @return Return the delete result
	 */
	public Boolean deleteResource(String uid, String privateKey, String url) {
		try {
			return hubService.deleteResource(uid, privateKey, url);

		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Resource owner creates permission to access dataHub for other user.
	 * 
	 * One user can only request the resource owner add grant permission, and only
	 * the resource owner can grant permission to other user.
	 * 
	 * @param createPermission Create permission information
	 * @return Return the crate permission information
	 */
	public CreatePermissionResp createPermission(CreatePermission createPermission) {
		try {
			return hubService.createPermission(createPermission);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * The user logically deletes the grant that has been created but has not been
	 * used.
	 * 
	 * Deleting permission can only be invoked by the resource owner. the permission
	 * record that has not been used is logically deleted.
	 * 
	 * @param deletePermission Permission Information
	 * @return Return the delete permission information
	 */
	public DeletePermissionResp deletePermission(DeletePermission deletePermission) {
		try {
			return hubService.deletePermission(deletePermission);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Users query the permissions of authorized third-party visitors.
	 * 
	 * Users can query all or part of the grant records from the grant's hubId and
	 * whether they have used.
	 * 
	 * @param queryPermission Query permission condition
	 * @return Return the permission Information list
	 */
	public List<PermissionInfo> queryPermission(QueryPermission queryPermission) {
		try {
			return hubService.queryPermission(queryPermission);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Query the granted permission information list to yourself
	 * 
	 * Users can query all or part of the authorization records from the ownner's
	 * hubId and whether they have used.
	 * 
	 * @param queryPermission Query permission condition
	 * @return Return the permission Information list
	 */
	public List<GrantPermissionInfo> queryGrantedPermission(QueryGrantedPermission queryPermission) {
		try {
			return hubService.queryGrantPermission(queryPermission);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Check if one hub user have WREAD/WRITE/UPDATE/DELTE permission for a
	 * resource. The resource owner have the resource WREAD/WRITE/UPDATE/DELTE
	 * permission. THe granted permission user have the granted user.
	 * 
	 * @param checkPermission Permission permission and resource information
	 * @return Return the check permission result
	 */
	public CheckPermissionResp checkPermission(CheckPermission checkPermission) {
		try {
			return hubService.checkPermission(checkPermission);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Query the modification history operation records of your own resources,
	 * including save, update, and delete operations history.
	 * 
	 * @param queryResourceHistory Query resource history request param
	 * @return Return the resource operation history Information list
	 */
	public List<ResourceHistoryInfo> queryResourceHistory(QueryResourceHistory queryResourceHistory) {
		try {
			return hubService.queryResourceHistory(queryResourceHistory);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}

	/**
	 * Decrypt ciphertext secret key and ciphertext content, decrypt the secret key
	 * by Secp256k1 algorithm, decrypt the content by the AES-ECDSA algorithm.
	 * 
	 * @param content    the ciphertext content
	 * @param encptyKey  the ciphertext secret key
	 * @param privateKey the private key
	 * @return return the plaintext content
	 */
	public String decrypt(String content, String encptyKey, String privateKey) {
		return hubService.decrypt(content, encptyKey, privateKey);
	}
	
	/**
	 * Generate public and private key through mnemonics. 
	 * The mnemonic should consist of 16 English words.
	 * 
	 * The same mnemonic will generate the same public and private key.
	 * 
	 * @param mnemList English words
	 * @return return public and private key
	 */
	public static KeyPair generalKeyPairByMnemonic(List<String> mnemList) {
		return HubService.generalKeyPairByMnemonic(mnemList);
	} 
	
	/**
	 * Change the data's owner to the new user in the hub
	 * 
	 * This transfer change the data's owner and re-encryption key
	 * 
	 * @param transferOwner transfer data owner request parameter info
	 * @return return true if transfer the data owner to the new user success
	 */
	public Boolean transferOwner(TransferOwner transferOwner) {
		try {
			return hubService.transferOwner(transferOwner);
		} catch (HubException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.UNKNOWN_ERROR.getCode(), ErrorMessage.UNKNOWN_ERROR.getMessage()+e.getMessage());
		}
	}
	
	/**
	 * Get the identify hub's Crypto Type
	 * 
	 * @return return the identify hub's Crypto Type
	 */
	public CryptoType getHubCryptoType() {
		return hubService.getCryptoType();
	}
	
}
