package com.usama.calculatorproblemv2;

import java.util.ArrayList;
import java.util.List;

public class FormulaNode {

    String elementName;
    String formula;
    private List<FormulaNode> dependencies;
    private boolean visited;
    private boolean visiting;

    public FormulaNode(String elementName, String formula) {
	super();
	this.elementName = elementName;
	this.formula = formula;
        this.dependencies = new ArrayList<>(); // Initialize the dependencies list here

    }

    public String getElementName() {
	return elementName;
    }

    public void setElementName(String elementName) {
	this.elementName = elementName;
    }

    public String getFormula() {
	return formula;
    }

    public void setFormula(String formula) {
	this.formula = formula;
    }

    public List<FormulaNode> getDependencies() {
	return dependencies;
    }

    public void setDependencies(List<FormulaNode> dependencies) {
	this.dependencies = dependencies;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisiting() {
        return visiting;
    }

    public void setVisiting(boolean visiting) {
        this.visiting = visiting;
    }

    
    
}
