package com.usama.calculatorproblemv2.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;

@Entity
@Builder
public class CompanyParameterScore {

    @Id
    private String companyName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "risk_parameters")
    private Set<RiskParameters> parameters = new HashSet<>();


    public CompanyParameterScore() {
	super();
	// TODO Auto-generated constructor stub
    }

    public CompanyParameterScore(String companyName, Set<RiskParameters> parameters) {
	super();
	this.companyName = companyName;
	this.parameters = parameters;
    }

    public String getCompanyName() {
	return companyName;
    }

    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    public Set<RiskParameters> getParameters() {
	return parameters;
    }

    public void setParameters(Set<RiskParameters> parameters) {
	this.parameters = parameters;
    }

    @Override
    public String toString() {
	return "CompanyRiskScore [companyName=" + companyName + ", parameters=" + parameters + "]";
    }

}
