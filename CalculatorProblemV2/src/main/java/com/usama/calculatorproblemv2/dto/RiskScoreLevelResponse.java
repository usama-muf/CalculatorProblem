package com.usama.calculatorproblemv2.dto;

public class RiskScoreLevelResponse {

    private boolean isInterfering;
    private String message;

    public RiskScoreLevelResponse() {

    }

    public boolean isInterfering() {
	return isInterfering;
    }

    public void setInterfering(boolean isInterfering) {
	this.isInterfering = isInterfering;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

}
