package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RiskScoreLevel {

	@Id
	@GeneratedValue
	private Long id;

	private String score;
	private String level;

	public RiskScoreLevel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "RiskScoreLevel [id=" + id + ", score=" + score + ", level=" + level + "]";
	}

}
