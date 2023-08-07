package com.usama.calculatorproblemv2.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
@Builder
public class CompanyRiskScore {
	
	@Id
	private String companyName;


	@ElementCollection
	@CollectionTable(name = "risk_parameters")
    private Set<RiskParameters> parameters = new HashSet<>();

    public CompanyRiskScore() {
	super();
	// TODO Auto-generated constructor stub
    }

    public CompanyRiskScore(String companyName, Set<RiskParameters> parameters) {
	super();
	this.companyName = companyName;
	this.parameters = parameters;
    }	
	
//	@ElementCollection
//	@CollectionTable(name = "risk_parameters", joinColumns = @JoinColumn(name = "company_risk_score_id"))
//	@MapKeyColumn(name = "parameter_name")
//	@Column(name = "parameter_value")
//	private Map<String, String> parameters;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

//	public Map<String, String> getParameters() {
//		return parameters;
//	}
//
//	public void setParameters(Map<String, String> parameters) {
//		this.parameters = parameters;
//	}

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
