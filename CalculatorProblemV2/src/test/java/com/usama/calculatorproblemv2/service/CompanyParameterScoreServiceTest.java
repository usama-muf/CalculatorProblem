package com.usama.calculatorproblemv2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.usama.calculatorproblemv2.dto.CompanyRiskScoreRequest;
import com.usama.calculatorproblemv2.entity.CompanyParameterScore;
import com.usama.calculatorproblemv2.repo.CompanyParameterScoreRepo;

public class CompanyParameterScoreServiceTest {

//    
    @InjectMocks
    private CompanyParameterScoreService companyRiskScoreService;

    @Mock
    private CompanyParameterScoreRepo companyRiskScoreRepository;

    @Mock
    private RiskDimensionService riskDimensionTableUpdater;

    @BeforeEach
    public void setUp() {
	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNewRiskScoreRow_Success() {
	// Arrange
	CompanyRiskScoreRequest request = new CompanyRiskScoreRequest();
	request.setCompanyName("Example Company");
	Map<String, Integer> parameters = new HashMap<>();
	parameters.put("Parameter1", 10);
	parameters.put("Parameter2", 20);
	request.setParameters(parameters);

	// Mock repository behavior
	Mockito.when(companyRiskScoreRepository.save(any(CompanyParameterScore.class))).thenReturn(new CompanyParameterScore());

	// Mock updater behavior

	// Act
	String result = companyRiskScoreService.createNewRiskScoreRow(request);

	// Assert
//        assertEquals("Table created successfully.", result);
	Mockito.verify(companyRiskScoreRepository, times(1)).save(any(CompanyParameterScore.class));
//        Mockito.verify(riskDimensionTableUpdater, times(1)).updateRiskDimensionTable();
    }

    @Test
    public void testCreateNewRiskScoreRow_Failure() {
	// Arrange
	CompanyRiskScoreRequest request = new CompanyRiskScoreRequest();
	request.setCompanyName("Example Company");
	Map<String, Integer> parameters = new HashMap<>();
	parameters.put("Parameter1", 10);
	parameters.put("Parameter2", 20);
	request.setParameters(parameters);

	// Mock repository behavior to throw an exception
	Mockito.when(companyRiskScoreRepository.save(any(CompanyParameterScore.class)))
		.thenThrow(new RuntimeException("Save failed."));

	// Act
	String result = companyRiskScoreService.createNewRiskScoreRow(request);

	// Assert
	assertEquals("Error creating table.", result);
	Mockito.verify(companyRiskScoreRepository, times(1)).save(any(CompanyParameterScore.class));
    }

    @Test
    public void testFindByCompanyNameBase_NullCompanyName_ThrowsRuntimeException() {
	// Arrange
	String companyName = null;

	// Act & Assert
	Assertions.assertThrows(RuntimeException.class, () -> {
	    companyRiskScoreService.findByCompanyName(companyName);
	});
    }

    @Test
    public void testFindByCompanyNameBase_EmptyCompanyName_ThrowsRuntimeException() {
	// Arrange
	String companyName = "";

	// Act & Assert
	Assertions.assertThrows(RuntimeException.class, () -> {
	    companyRiskScoreService.findByCompanyName(companyName);
	});
    }

    @Test
    public void testFindByCompanyNameBase_ExceptionInRepository_ThrowsRuntimeException() {
	// Arrange
	String companyName = "Exceptional Company";

	// Act & Assert
	Assertions.assertThrows(RuntimeException.class, () -> {
	    companyRiskScoreService.findByCompanyName(companyName);
	});
    }

}
