package com.reddate.hub.sdk.exception;

/**
 * Did SDK common exception definition, HubException is a runtime exception, no
 * need catch this exception in code.
 */
public class HubException extends RuntimeException {

	/**
	 * exception error code
	 */
	private Integer code;

	public HubException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

}
