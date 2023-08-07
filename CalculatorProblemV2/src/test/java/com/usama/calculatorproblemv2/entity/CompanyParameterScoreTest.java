package com.usama.calculatorproblemv2.entity;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public class CompanyParameterScoreTest {
    @Test
    @DisplayName("Test Entity Mapping")
    public void testEntityMapping() {
	Assertions.assertTrue(CompanyParameterScore.class.isAnnotationPresent(Entity.class));
    }

    @Test
    @DisplayName("Test ID Field")
    public void testIdField() throws NoSuchFieldException {
	Assertions.assertTrue(CompanyParameterScore.class.getDeclaredField("companyName").isAnnotationPresent(Id.class));
	Assertions.assertEquals(String.class, CompanyParameterScore.class.getDeclaredField("companyName").getType());
    }

    @Test
    @DisplayName("Test Parameter Collection")
    public void testParameterCollection() throws NoSuchFieldException {
	Assertions.assertTrue(
		CompanyParameterScore.class.getDeclaredField("parameters").isAnnotationPresent(ElementCollection.class));
	Assertions.assertEquals(Set.class, CompanyParameterScore.class.getDeclaredField("parameters").getType());
	CompanyParameterScore riskScore = new CompanyParameterScore();
	Assertions.assertNotNull(riskScore.getParameters());
	RiskParameters riskParameters = new RiskParameters();
	riskScore.getParameters().add(riskParameters);
	Assertions.assertEquals(1, riskScore.getParameters().size());
    }

    @Test
    @DisplayName("Test Collection Table Name")
    public void testCollectionTableName() throws NoSuchFieldException {
	CollectionTable collectionTable = CompanyParameterScore.class.getDeclaredField("parameters")
		.getAnnotation(CollectionTable.class);
	Assertions.assertNotNull(collectionTable);
	Assertions.assertEquals("risk_parameters", collectionTable.name());
    }

    @Test
    @DisplayName("Test Set Behavior")
    public void testSetBehavior() {
	CompanyParameterScore riskScore = new CompanyParameterScore();
	RiskParameters riskParameters = new RiskParameters("param1" ,1);
	riskScore.getParameters().add(riskParameters);
	riskScore.getParameters().add(riskParameters); // Adding duplicate
	Assertions.assertEquals(1, riskScore.getParameters().size());
	riskScore.getParameters().remove(riskParameters);
	Assertions.assertTrue(riskScore.getParameters().isEmpty());
    }
    
    @Test
    @DisplayName("Test Valid Company Name")
    public void testValidCompanyName() {
        CompanyParameterScore temp = new CompanyParameterScore();
        temp.setCompanyName("CompanyA");
        Assertions.assertEquals("CompanyA", temp.getCompanyName());
    }

    @Test
    @DisplayName("Test Null Company Name")
    public void testNullCompanyName() {
        CompanyParameterScore temp = new CompanyParameterScore();
        temp.setCompanyName(null);
        Assertions.assertNull(temp.getCompanyName());
    }

    @Test
    @DisplayName("Test Empty Company Name")
    public void testEmptyCompanyName() {
        CompanyParameterScore temp = new CompanyParameterScore();
        temp.setCompanyName("");
        Assertions.assertEquals("", temp.getCompanyName());
    }

    @Test
    @DisplayName("Test Valid Parameters")
    public void testValidParameters() {
        CompanyParameterScore temp = new CompanyParameterScore();
        Set<RiskParameters> parameters = new HashSet<>();
        parameters.add(new RiskParameters("Parameter1", 1));
        temp.setParameters(parameters);

        Set<RiskParameters> retrievedParameters = temp.getParameters();
        Assertions.assertEquals(parameters, retrievedParameters);
        Assertions.assertEquals(1, retrievedParameters.size());
    }

    @Test
    @DisplayName("Test Null Parameters")
    public void testNullParameters() {
        CompanyParameterScore temp = new CompanyParameterScore();
        temp.setParameters(null);
        Assertions.assertNull(temp.getParameters());
    }

    @Test
    @DisplayName("Test Empty Parameters")
    public void testEmptyParameters() {
        CompanyParameterScore temp = new CompanyParameterScore();
        Set<RiskParameters> parameters = new HashSet();
        temp.setParameters(parameters);

        Set<RiskParameters> retrievedParameters = temp.getParameters();
        Assertions.assertEquals(parameters, retrievedParameters);
        Assertions.assertEquals(0, retrievedParameters.size());
    }

    @Test
    @DisplayName("Test Valid Parameter Name and Value")
    public void testValidParameterNameAndValue() {
        RiskParameters riskParameters = new RiskParameters();
        riskParameters.setParameterName("Parameter1");
        riskParameters.setParameterValue(1);

        Assertions.assertEquals("Parameter1", riskParameters.getParameterName());
        Assertions.assertEquals(1, riskParameters.getParameterValue());
    }

    @Test
    @DisplayName("Test Null Parameter Name and Value")
    public void testNullParameterNameAndValue() {
        RiskParameters riskParameters = new RiskParameters();
        riskParameters.setParameterName(null);
        riskParameters.setParameterValue(null);

        Assertions.assertNull(riskParameters.getParameterName());
        Assertions.assertNull(riskParameters.getParameterValue());
    }

    @Test
    @DisplayName("Test Empty Parameter Name and Value")
    public void testEmptyParameterNameAndValue() {
        RiskParameters riskParameters = new RiskParameters();
        riskParameters.setParameterName("");
        riskParameters.setParameterValue(null);

        Assertions.assertEquals("", riskParameters.getParameterName());
        Assertions.assertEquals(null , riskParameters.getParameterValue());
    }

}
