package com.reddate.hub.service.vo.request;

import java.io.Serializable;

public class SignInfoReq implements Serializable {

	private String message;
	
	private String address;
	
	private String signValue;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSignValue() {
		return signValue;
	}

	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	
	
	
	
}
