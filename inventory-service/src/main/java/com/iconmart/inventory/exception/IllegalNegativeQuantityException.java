package com.iconmart.inventory.exception;

public class IllegalNegativeQuantityException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code = "ICONMARTNEG12";

	public IllegalNegativeQuantityException(String status) {
		super(status);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
