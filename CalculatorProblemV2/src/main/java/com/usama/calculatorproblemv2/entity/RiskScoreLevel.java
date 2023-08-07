package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RiskScoreLevel {

    @Id
    @GeneratedValue
    private Long id;

//	private String score;
    private int minScore;
    private int maxScore;

    @Column(nullable = false, unique = true)
    private String level;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

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

    public String getLevel() {
	return level;
    }

    public void setLevel(String level) {
	this.level = level;
    }

    public RiskScoreLevel() {
	super();
	// TODO Auto-generated constructor stub
    }

    public RiskScoreLevel(int minScore, int maxScore, String level) {
	this.minScore = minScore;
	this.maxScore = maxScore;
	this.level = level;
    }

    @Override
    public String toString() {
	return "RiskScoreLevel [id=" + id + ", minScore=" + minScore + ", maxScore=" + maxScore + ", level=" + level
		+ "]";
    }

}
