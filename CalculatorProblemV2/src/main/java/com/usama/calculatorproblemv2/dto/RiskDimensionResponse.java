package com.usama.calculatorproblemv2.dto;

public class RiskDimensionResponse {

    private boolean isValid;
    private String message;

    public RiskDimensionResponse() {
    }

    public RiskDimensionResponse(boolean isValid, String message) {
	super();
	this.isValid = isValid;
	this.message = message;
    }

    public boolean isValid() {
	return isValid;
    }

    public void setValid(boolean isValid) {
	this.isValid = isValid;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

}
