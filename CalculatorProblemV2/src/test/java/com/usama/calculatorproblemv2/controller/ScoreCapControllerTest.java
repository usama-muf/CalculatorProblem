package com.usama.calculatorproblemv2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.usama.calculatorproblemv2.dto.ScoreCapPutRequest;
import com.usama.calculatorproblemv2.entity.ScoreCap;
import com.usama.calculatorproblemv2.service.ScoreCapService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ScoreCapController.class)
public class ScoreCapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreCapService scoreCapService;

    private final String baseUri = "/v1/api/sc";
    
    @Test
    void testCreateNewScoreCap() throws Exception {
        // Arrange
        ScoreCap capRequest = new ScoreCap();
        capRequest.setScoreCapCondition("Condition");
        capRequest.setCountCondition(10);
        capRequest.setTotalRiskCappedScore(100);

        ScoreCap capResponse = new ScoreCap();
        capResponse.setId(1L);
        capResponse.setScoreCapCondition("Condition");
        capResponse.setCountCondition(10);
        capResponse.setTotalRiskCappedScore(100);

        when(scoreCapService.createNewScoreCap(any(ScoreCap.class))).thenReturn(capResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post(baseUri+"/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"scoreCapCondition\": \"Condition\", \"countCondition\": 10, \"totalRiskCappedScore\": 100}")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scoreCapCondition").value("Condition"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countCondition").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalRiskCappedScore").value(100));
    }
    
    @Test
    void testGetAllScoreCap() throws Exception {
        // Arrange
        ScoreCap cap1 = new ScoreCap();
        cap1.setId(1L);
        cap1.setScoreCapCondition("Condition 1");
        cap1.setCountCondition(10);
        cap1.setTotalRiskCappedScore(100);

        ScoreCap cap2 = new ScoreCap();
        cap2.setId(2L);
        cap2.setScoreCapCondition("Condition 2");
        cap2.setCountCondition(20);
        cap2.setTotalRiskCappedScore(200);

        List<ScoreCap> scoreCapList = Arrays.asList(cap1, cap2);

        when(scoreCapService.getAllScoreCap()).thenReturn(scoreCapList);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri+"/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scoreCapCondition").value("Condition 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countCondition").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalRiskCappedScore").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].scoreCapCondition").value("Condition 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].countCondition").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].totalRiskCappedScore").value(200));
    }
    
    @Test
    void testGetScoreCapByIdExistingId() throws Exception {
        // Arrange
        Long id = 1L;
        ScoreCap cap = new ScoreCap();
        cap.setId(id);
        cap.setScoreCapCondition("Condition");
        cap.setCountCondition(10);
        cap.setTotalRiskCappedScore(100);

        when(scoreCapService.getScoreCapById(eq(id))).thenReturn(Optional.of(cap));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri+"/get/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scoreCapCondition").value("Condition"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countCondition").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalRiskCappedScore").value(100));
    }

    @Test
    void testGetScoreCapByIdNonExistingId() throws Exception {
        // Arrange
        Long id = 1L;
        when(scoreCapService.getScoreCapById(eq(id))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/get/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    void testUpdateScoreCapDataSuccessful() throws Exception {
	// Arrange
	Long id = 1L;
	ScoreCapPutRequest request = new ScoreCapPutRequest(); // Create a valid request object
	when(scoreCapService.updateScoreCapData(eq(id), any(ScoreCapPutRequest.class))).thenReturn(true);

	// Act & Assert
	mockMvc.perform(MockMvcRequestBuilders.put(baseUri+"/update/{id}", id).contentType(MediaType.APPLICATION_JSON)
		.content("{\"value\": 45}")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateScoreCapDataNotFound() throws Exception {
	// Arrange
	Long id = 1L;
	ScoreCapPutRequest request = new ScoreCapPutRequest(); // Create a valid request object
	when(scoreCapService.updateScoreCapData(eq(id), any(ScoreCapPutRequest.class))).thenReturn(false);

	// Act & Assert
	mockMvc.perform(MockMvcRequestBuilders.put(baseUri+"/update/{id}", id).contentType(MediaType.APPLICATION_JSON)
		.content("{\"value\": 45}")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateScoreCapDataException() throws Exception {
	// Arrange
	Long id = 1L;
	ScoreCapPutRequest request = new ScoreCapPutRequest(); // Create a valid request object
	when(scoreCapService.updateScoreCapData(eq(id), any(ScoreCapPutRequest.class)))
		.thenThrow(new RuntimeException("Test Exception"));

	// Act & Assert
	mockMvc.perform(MockMvcRequestBuilders.put(baseUri+"/update/{id}", id).contentType(MediaType.APPLICATION_JSON)
		.content("{\"value\": 45}")).andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testDeleteRiskScoreLevelSuccessful() throws Exception {
        // Arrange
        Long id = 1L;
        when(scoreCapService.deleteScoreCapData(eq(id))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUri+"/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteRiskScoreLevelNotFound() throws Exception {
        // Arrange
        Long id = 1L;
        when(scoreCapService.deleteScoreCapData(eq(id))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteRiskScoreLevelServerError() throws Exception {
        // Arrange
        Long id = 1L;
        when(scoreCapService.deleteScoreCapData(eq(id))).thenThrow(new RuntimeException("Something went wrong"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUri+"/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
