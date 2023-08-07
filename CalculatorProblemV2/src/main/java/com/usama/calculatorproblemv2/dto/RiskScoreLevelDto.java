package com.usama.calculatorproblemv2.dto;

public class RiskScoreLevelDto {

    private Integer minScore;
    private Integer maxScore;
    private String level;

    public RiskScoreLevelDto() {
    }

    public RiskScoreLevelDto(Integer minScore, Integer maxScore, String level) {
	super();
	this.minScore = minScore;
	this.maxScore = maxScore;
	this.level = level;
    }

    public Integer getMinScore() {
	return minScore;
    }

    public void setMinScore(Integer minScore) {
	this.minScore = minScore;
    }

    public Integer getMaxScore() {
	return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
	this.maxScore = maxScore;
    }

    public String getLevel() {
	return level;
    }

    public void setLevel(String level) {
	this.level = level;
    }

    @Override
    public String toString() {
	return "RiskScoreLevelDto [minScore=" + minScore + ", maxScore=" + maxScore + ", level=" + level + "]";
    }

}
