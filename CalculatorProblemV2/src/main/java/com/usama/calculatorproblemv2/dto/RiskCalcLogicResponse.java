package com.usama.calculatorproblemv2.dto;

public class RiskCalcLogicResponse {

    private boolean isValid;
    private String message;

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

    public RiskCalcLogicResponse() {

    }

}
