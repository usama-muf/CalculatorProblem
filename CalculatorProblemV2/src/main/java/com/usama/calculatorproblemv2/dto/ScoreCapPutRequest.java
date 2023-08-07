package com.usama.calculatorproblemv2.dto;

public class ScoreCapPutRequest {

    @Override
    public String toString() {
	return "ScoreCapPutRequest [value=" + value + "]";
    }

    private Integer value;

    public ScoreCapPutRequest() {
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }

}
