package com.usama.calculatorproblemv2.dto;

public class RiskScoreLevelPutDto {

    private int minScore;
    private int maxScore;

    public int getMinScore() {
	return minScore;
    }

    public void setMinScore(int minScore) {
	this.minScore = minScore;
    }

    public int getMaxScore() {
	return maxScore;
    }

    public void setMaxScore(int maxScore) {
	this.maxScore = maxScore;
    }

    public RiskScoreLevelPutDto() {
    }

}
