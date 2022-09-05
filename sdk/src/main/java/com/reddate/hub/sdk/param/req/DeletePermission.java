package com.reddate.hub.sdk.param.req;

import java.io.Serializable;

/**
 * Delete permission request data structure
 */
public class DeletePermission implements Serializable {

	/**
	 * identify hub user hub
	 */
	private String uid;

	/**
	 * resource url
	 */
	private String url;

	/**
	 * grant identify hub user hub
	 */
	private String grantUid;

	/**
	 * grant information
	 */
	private Operation grant;

	/**
	 * identify hub user private key
	 */
	private String privateKey;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGrantUid() {
		return grantUid;
	}

	public void setGrantUid(String grantUid) {
		this.grantUid = grantUid;
	}

	public Operation getGrant() {
		return grant;
	}

	public void setGrant(Operation grant) {
		this.grant = grant;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
