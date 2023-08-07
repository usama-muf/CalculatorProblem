package com.usama.calculatorproblemv2.dto;

import java.util.ArrayList;
import java.util.List;

import com.usama.calculatorproblemv2.entity.Output;

public class OutputResponse {

    private List<Output> outputData = new ArrayList<>();

    public OutputResponse() {
    }

    public List<Output> getOutputs() {
	return outputData;
    }

    public void setOutputs(List<Output> outputData) {
	this.outputData = outputData;
    }

}
