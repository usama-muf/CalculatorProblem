package com.usama.calculatorproblemv2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.usama.calculatorproblemv2.entity.RiskScoreLevel;
import com.usama.calculatorproblemv2.service.RiskScoreLevelService;

@WebMvcTest(RiskScoreLevelController.class)

public class RiskScoreLevelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiskScoreLevelService riskScoreLevelService;

    private static String baseUri = "/v1/api/rs";

    @Test
    public void testGetAllRiskScoreLevel_Success() {
	// Prepare test data: a list of RiskScoreLevel objects
	List<RiskScoreLevel> testData = new ArrayList<>();
	testData.add(new RiskScoreLevel(1, 100, "Low"));
	testData.add(new RiskScoreLevel(101, 200, "Medium"));

	// Mock the behavior of riskScoreLevelService.getAllRiskScoreLevels() to return
	// the test data
	when(riskScoreLevelService.getAllRiskScoreLevels()).thenReturn(testData);

	// Call the getAllRiskScoreLevel method of riskScoreLevelController
	List<RiskScoreLevel> result = this.riskScoreLevelService.getAllRiskScoreLevels();

	// Assert the expected result
	assertEquals(testData, result);

	// Verify that the riskScoreLevelService method was called
	verify(riskScoreLevelService).getAllRiskScoreLevels();
    }

    @Test
    public void testGetAllRiskScoreLevel_EmptyData() {
	// Prepare test data: an empty list
	List<RiskScoreLevel> testData = new ArrayList<>();

	// Mock the behavior of riskScoreLevelService.getAllRiskScoreLevels() to return
	// an empty list
	when(riskScoreLevelService.getAllRiskScoreLevels()).thenReturn(testData);

	// Call the getAllRiskScoreLevel method of riskScoreLevelController
	List<RiskScoreLevel> result = this.riskScoreLevelService.getAllRiskScoreLevels();

	// Assert the expected result
	assertTrue(result.isEmpty());

	// Verify that the riskScoreLevelService method was called
	verify(riskScoreLevelService).getAllRiskScoreLevels();
    }

    @Test
    public void testGetRiskScoreLevel_Success() throws Exception {
	String levelName = "Low";

	// Prepare test data: a RiskScoreLevel object
	RiskScoreLevel testData = new RiskScoreLevel(1, 100, levelName);

	// Mock the behavior of riskScoreLevelService.getRiskScoreLevel() to return the
	// test data
	when(riskScoreLevelService.getRiskScoreLevel(levelName)).thenReturn(testData);

	// Perform the GET request to "/get/{levelName}" endpoint
	mockMvc.perform(MockMvcRequestBuilders.put(baseUri + "/get/{levelName}", levelName)
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(jsonPath("$.minScore").value(testData.getMinScore()))
		.andExpect(jsonPath("$.maxScore").value(testData.getMaxScore()))
		.andExpect(jsonPath("$.level").value(testData.getLevel()));

	// Verify that the riskScoreLevelService method was called with the correct
	// parameter
	verify(riskScoreLevelService).getRiskScoreLevel(levelName);
    }

    @Test
    public void testGetRiskScoreLevel_NotFound() throws Exception {
	String levelName = "NonExistentLevel";

	// Mock the behavior of riskScoreLevelService.getRiskScoreLevel() to return null
	when(riskScoreLevelService.getRiskScoreLevel(levelName)).thenReturn(null);

	// Perform the GET request to "/get/{levelName}" endpoint
	mockMvc.perform(get(baseUri + "/get/{levelName}", levelName)).andExpect(status().isNotFound());
//                .andExpect(jsonPath("$").doesNotExist());

	// Verify that the riskScoreLevelService method was called with the correct
	// parameter
	verify(riskScoreLevelService).getRiskScoreLevel(levelName);
    }

    @Test
    public void testGetRiskScoreLevel_ExceptionHandling() throws Exception {
	String levelName = "Low";

	// Mock the behavior of riskScoreLevelService.getRiskScoreLevel() to throw an
	// exception
	when(riskScoreLevelService.getRiskScoreLevel(levelName))
		.thenThrow(new RuntimeException("Failed to fetch data"));

	// Perform the GET request to "/get/{levelName}" endpoint
	mockMvc.perform(get("/get/{levelName}", levelName)).andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$").doesNotExist());

	// Verify that the riskScoreLevelService method was called with the correct
	// parameter
	verify(riskScoreLevelService).getRiskScoreLevel(levelName);
    }

}
