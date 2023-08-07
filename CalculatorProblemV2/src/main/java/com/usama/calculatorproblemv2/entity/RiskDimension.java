package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class RiskDimension {

	@Id
	private String dimension;
	private Integer weight;

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "RiskDimension [dimension=" + dimension + ", weight=" + weight + "]";
	}

}
