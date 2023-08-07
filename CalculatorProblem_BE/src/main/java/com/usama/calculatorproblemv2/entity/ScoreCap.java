package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@Table(name = "score_cap")
public class ScoreCap {

	@Id
	@GeneratedValue
	private Long id;

	private String scoreCapCondition;
	private Integer countCondition;
	private Integer totalRiskCappedScore;

	public ScoreCap() {	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScoreCapCondition() {
		return scoreCapCondition;
	}

	public void setScoreCapCondition(String scoreCapCondition) {
		this.scoreCapCondition = scoreCapCondition;
	}

	public Integer getCountCondition() {
		return countCondition;
	}

	public void setCountCondition(Integer countCondition) {
		this.countCondition = countCondition;
	}

	public Integer getTotalRiskCappedScore() {
		return totalRiskCappedScore;
	}

	public void setTotalRiskCappedScore(Integer totalRiskCappedScore) {
		this.totalRiskCappedScore = totalRiskCappedScore;
	}

}
