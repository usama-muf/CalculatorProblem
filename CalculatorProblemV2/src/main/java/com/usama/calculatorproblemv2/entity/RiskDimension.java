package com.usama.calculatorproblemv2.entity;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class RiskDimension {

    
    @Id
    private String dimension;
    
    @Min(value = 0, message = "Weight percent cannot be negative")
    @Max(value = 100, message = "Weight percent cannot be greater than 100")
    @Column(nullable = false, columnDefinition = "CHECK")
    
    private Integer weight;

    public RiskDimension() {

    }

    public RiskDimension(String dimension, Integer weight) {
	super();
	this.dimension = dimension;
	this.weight = weight;
    }

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
