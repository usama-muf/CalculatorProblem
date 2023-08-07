package com.usama.calculatorproblemv2.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Output {

	@Id
	private String companyName;

	@ElementCollection
	@CollectionTable(name = "output_parameter")
	private Set<OutputParameters> outputParameters = new HashSet<>();

	public Output() {
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Set<OutputParameters> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(Set<OutputParameters> outputParameters) {
		this.outputParameters = outputParameters;
	}

}
