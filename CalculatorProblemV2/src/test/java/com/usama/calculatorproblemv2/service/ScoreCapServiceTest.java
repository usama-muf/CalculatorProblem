package com.usama.calculatorproblemv2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.usama.calculatorproblemv2.dto.ScoreCapPutRequest;
import com.usama.calculatorproblemv2.entity.ScoreCap;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;

public class ScoreCapServiceTest {

    @InjectMocks
    private ScoreCapService scoreCapService;

    @Mock
    private ScoreCapRepo scoreCapRepo;

    @BeforeEach
    void setUp() {
	MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewScoreCap_Success() {
	// Arrange
	ScoreCap inputScoreCap = new ScoreCap();
	inputScoreCap.setScoreCapCondition("Condition");
	inputScoreCap.setCountCondition(50);
	inputScoreCap.setTotalRiskCappedScore(100);

	ScoreCap savedScoreCap = new ScoreCap();
	savedScoreCap.setId(1L);
	savedScoreCap.setScoreCapCondition(inputScoreCap.getScoreCapCondition());
	savedScoreCap.setCountCondition(inputScoreCap.getCountCondition());
	savedScoreCap.setTotalRiskCappedScore(inputScoreCap.getTotalRiskCappedScore());

	when(scoreCapRepo.save(inputScoreCap)).thenReturn(savedScoreCap);

	// Act
	ScoreCap result = scoreCapService.createNewScoreCap(inputScoreCap);

	// Assert
	assertNotNull(result);
	assertEquals(savedScoreCap.getId(), result.getId());
	assertEquals(savedScoreCap.getScoreCapCondition(), result.getScoreCapCondition());
	assertEquals(savedScoreCap.getCountCondition(), result.getCountCondition());
	assertEquals(savedScoreCap.getTotalRiskCappedScore(), result.getTotalRiskCappedScore());

	verify(scoreCapRepo, times(1)).save(inputScoreCap);
    }

    @Test
    void testCreateNewScoreCap_InvalidInput_Null() {
	// Arrange
	ScoreCap inputScoreCap = null;

	// Act
	ScoreCap result = scoreCapService.createNewScoreCap(inputScoreCap);

	// Assert
	assertNull(result); // Expecting null as the input is invalid

	verify(scoreCapRepo, never()).save(any());
    }

    @Test
    void testCreateNewScoreCap_InvalidInput_InvalidCountCondition() {
	// Arrange
	ScoreCap inputScoreCap = new ScoreCap();
	inputScoreCap.setScoreCapCondition("Condition");
	inputScoreCap.setCountCondition(-50);
	inputScoreCap.setTotalRiskCappedScore(100);

	// Act
	ScoreCap result = scoreCapService.createNewScoreCap(inputScoreCap);

	// Assert
	assertNull(result); // Expecting null as the countCondition is invalid

	verify(scoreCapRepo, never()).save(any());
    }

    @Test
    void testCreateNewScoreCap_InvalidInput_InvalidTotalRiskCappedScore() {
	// Arrange
	ScoreCap inputScoreCap = new ScoreCap();
	inputScoreCap.setScoreCapCondition("Condition");
	inputScoreCap.setCountCondition(50);
	inputScoreCap.setTotalRiskCappedScore(-100);

	// Act
	ScoreCap result = scoreCapService.createNewScoreCap(inputScoreCap);

	// Assert
	assertNull(result); // Expecting null as the totalRiskCappedScore is invalid

	verify(scoreCapRepo, never()).save(any());
    }

    @Test
    void testCreateNewScoreCap_InvalidInput_EmptyScoreCapCondition() {
	// Arrange
	ScoreCap inputScoreCap = new ScoreCap();
	inputScoreCap.setScoreCapCondition("");
	inputScoreCap.setCountCondition(50);
	inputScoreCap.setTotalRiskCappedScore(100);

	// Act
	ScoreCap result = scoreCapService.createNewScoreCap(inputScoreCap);

	// Assert
	assertNull(result); // Expecting null as the scoreCapCondition is empty

	verify(scoreCapRepo, never()).save(any());
    }

    @Test
    void testCreateNewScoreCap_ExceptionDuringSaving() {
	// Arrange
	ScoreCap inputScoreCap = new ScoreCap();
	inputScoreCap.setScoreCapCondition("Condition");
	inputScoreCap.setCountCondition(50);
	inputScoreCap.setTotalRiskCappedScore(100);

	when(scoreCapRepo.save(inputScoreCap)).thenThrow(new RuntimeException("Error saving ScoreCap"));

	// Act
	ScoreCap result = scoreCapService.createNewScoreCap(inputScoreCap);

	// Assert
	assertNull(result); // Expecting null due to the exception

	verify(scoreCapRepo, times(1)).save(inputScoreCap);
    }

    
    @Test
    void testGetAllScoreCap_Success() {
        // Arrange
        ScoreCap scoreCap1 = new ScoreCap();
        scoreCap1.setId(1L);

        ScoreCap scoreCap2 = new ScoreCap();
        scoreCap2.setId(2L);

        List<ScoreCap> scoreCapList = new ArrayList<>();
        scoreCapList.add(scoreCap1);
        scoreCapList.add(scoreCap2);

        when(scoreCapRepo.findAll()).thenReturn(scoreCapList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(scoreCap1.getId(), result.get(0).getId());
        assertEquals(scoreCap2.getId(), result.get(1).getId());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_EmptyRepository() {
        // Arrange
        List<ScoreCap> emptyList = new ArrayList<>();
        when(scoreCapRepo.findAll()).thenReturn(emptyList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_MultipleEntries() {
        // Arrange
        ScoreCap scoreCap1 = new ScoreCap();
        scoreCap1.setId(1L);

        ScoreCap scoreCap2 = new ScoreCap();
        scoreCap2.setId(2L);

        List<ScoreCap> scoreCapList = new ArrayList<>();
        scoreCapList.add(scoreCap1);
        scoreCapList.add(scoreCap2);

        when(scoreCapRepo.findAll()).thenReturn(scoreCapList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(scoreCap1.getId(), result.get(0).getId());
        assertEquals(scoreCap2.getId(), result.get(1).getId());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_SpecificAttributes() {
        // Arrange
        ScoreCap scoreCap1 = new ScoreCap();
        scoreCap1.setId(1L);
        scoreCap1.setScoreCapCondition("Condition1");
        scoreCap1.setCountCondition(50);
        scoreCap1.setTotalRiskCappedScore(100);

        ScoreCap scoreCap2 = new ScoreCap();
        scoreCap2.setId(2L);
        scoreCap2.setScoreCapCondition("Condition2");
        scoreCap2.setCountCondition(70);
        scoreCap2.setTotalRiskCappedScore(200);

        List<ScoreCap> scoreCapList = new ArrayList<>();
        scoreCapList.add(scoreCap1);
        scoreCapList.add(scoreCap2);

        when(scoreCapRepo.findAll()).thenReturn(scoreCapList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        ScoreCap resultScoreCap1 = result.get(0);
        assertEquals(scoreCap1.getId(), resultScoreCap1.getId());
        assertEquals(scoreCap1.getScoreCapCondition(), resultScoreCap1.getScoreCapCondition());
        assertEquals(scoreCap1.getCountCondition(), resultScoreCap1.getCountCondition());
        assertEquals(scoreCap1.getTotalRiskCappedScore(), resultScoreCap1.getTotalRiskCappedScore());

        ScoreCap resultScoreCap2 = result.get(1);
        assertEquals(scoreCap2.getId(), resultScoreCap2.getId());
        assertEquals(scoreCap2.getScoreCapCondition(), resultScoreCap2.getScoreCapCondition());
        assertEquals(scoreCap2.getCountCondition(), resultScoreCap2.getCountCondition());
        assertEquals(scoreCap2.getTotalRiskCappedScore(), resultScoreCap2.getTotalRiskCappedScore());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_NumberOfElements() {
        // Arrange
        ScoreCap scoreCap1 = new ScoreCap();
        scoreCap1.setId(1L);

        ScoreCap scoreCap2 = new ScoreCap();
        scoreCap2.setId(2L);

        List<ScoreCap> scoreCapList = new ArrayList<>();
        scoreCapList.add(scoreCap1);
        scoreCapList.add(scoreCap2);

        when(scoreCapRepo.findAll()).thenReturn(scoreCapList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_OrderOfElements() {
        // Arrange
        ScoreCap scoreCap1 = new ScoreCap();
        scoreCap1.setId(1L);

        ScoreCap scoreCap2 = new ScoreCap();
        scoreCap2.setId(2L);

        List<ScoreCap> scoreCapList = new ArrayList<>();
        scoreCapList.add(scoreCap1);
        scoreCapList.add(scoreCap2);

        when(scoreCapRepo.findAll()).thenReturn(scoreCapList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(scoreCap1.getId(), result.get(0).getId());
        assertEquals(scoreCap2.getId(), result.get(1).getId());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_FindAllCalled() {
        // Arrange
        when(scoreCapRepo.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_IntegrityOfScoreCapObjects() {
        // Arrange
        ScoreCap scoreCap1 = new ScoreCap();
        scoreCap1.setId(1L);
        scoreCap1.setScoreCapCondition("Condition1");
        scoreCap1.setCountCondition(50);
        scoreCap1.setTotalRiskCappedScore(100);

        ScoreCap scoreCap2 = new ScoreCap();
        scoreCap2.setId(2L);
        scoreCap2.setScoreCapCondition("Condition2");
        scoreCap2.setCountCondition(70);
        scoreCap2.setTotalRiskCappedScore(200);

        List<ScoreCap> scoreCapList = new ArrayList<>();
        scoreCapList.add(scoreCap1);
        scoreCapList.add(scoreCap2);

        when(scoreCapRepo.findAll()).thenReturn(scoreCapList);

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        ScoreCap resultScoreCap1 = result.get(0);
        assertEquals(scoreCap1.getId(), resultScoreCap1.getId());
        assertEquals(scoreCap1.getScoreCapCondition(), resultScoreCap1.getScoreCapCondition());
        assertEquals(scoreCap1.getCountCondition(), resultScoreCap1.getCountCondition());
        assertEquals(scoreCap1.getTotalRiskCappedScore(), resultScoreCap1.getTotalRiskCappedScore());

        ScoreCap resultScoreCap2 = result.get(1);
        assertEquals(scoreCap2.getId(), resultScoreCap2.getId());
        assertEquals(scoreCap2.getScoreCapCondition(), resultScoreCap2.getScoreCapCondition());
        assertEquals(scoreCap2.getCountCondition(), resultScoreCap2.getCountCondition());
        assertEquals(scoreCap2.getTotalRiskCappedScore(), resultScoreCap2.getTotalRiskCappedScore());

        verify(scoreCapRepo, times(1)).findAll();
    }

    @Test
    void testGetAllScoreCap_ExceptionDuringRetrieval() {
        // Arrange
        when(scoreCapRepo.findAll()).thenThrow(new RuntimeException("Database connection failed."));

        // Act
        List<ScoreCap> result = scoreCapService.getAllScoreCap();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(scoreCapRepo, times(1)).findAll();
    }
    
    @Test
    void testDeleteScoreCapDataExistingId() {
	// Arrange
	Long id = 1L;
	ScoreCap existingScoreCap = new ScoreCap();
	existingScoreCap.setId(id);

	when(scoreCapRepo.findById(eq(id))).thenReturn(Optional.of(existingScoreCap));

	// Act
	boolean result = scoreCapService.deleteScoreCapData(id);

	// Assert
	assertTrue(result);
	verify(scoreCapRepo, times(1)).delete(eq(existingScoreCap));
    }

    @Test
    void testDeleteScoreCapDataNonExistingId() {
	// Arrange
	Long id = 1L;

	when(scoreCapRepo.findById(eq(id))).thenReturn(Optional.empty());

	// Act
	boolean result = scoreCapService.deleteScoreCapData(id);

	// Assert
	assertFalse(result);
	verify(scoreCapRepo, never()).delete(any());
    }

    @Test
    void testDeleteScoreCapDataInvalidId() {
	// Arrange
	Long invalidId = null; // Invalid ID (null is not a valid Long)

	// Act
	boolean result = scoreCapService.deleteScoreCapData(invalidId);

	// Assert
	assertFalse(result); // Expecting the method to return false
	verify(scoreCapRepo, never()).findById(any());
	verify(scoreCapRepo, never()).delete(any());
    }

    @Test
    void testDeleteScoreCapDataException() {
	// Arrange
	Long id = 1L;
	ScoreCap existingScoreCap = new ScoreCap();
	existingScoreCap.setId(id);

	when(scoreCapRepo.findById(eq(id))).thenReturn(Optional.of(existingScoreCap));
	doThrow(new RuntimeException("Something went wrong")).when(scoreCapRepo).delete(eq(existingScoreCap));

	// Act
	boolean result = scoreCapService.deleteScoreCapData(id);

	// Assert
	assertFalse(result);
	verify(scoreCapRepo, times(1)).delete(eq(existingScoreCap));
    }
    @Test
    void testUpdateScoreCapData_Success() {
        // Arrange
        Long id = 1L;
        ScoreCapPutRequest request = new ScoreCapPutRequest();
        request.setValue(200);

        ScoreCap scoreCap = new ScoreCap();
        scoreCap.setId(id);
        scoreCap.setTotalRiskCappedScore(100);

        when(scoreCapRepo.findById(id)).thenReturn(Optional.of(scoreCap));
        when(scoreCapRepo.save(any(ScoreCap.class))).thenReturn(scoreCap);

        // Act
        boolean result = scoreCapService.updateScoreCapData(id, request);

        // Assert
        assertTrue(result);
        assertEquals(200, scoreCap.getTotalRiskCappedScore());

        verify(scoreCapRepo, times(1)).findById(id);
        verify(scoreCapRepo, times(1)).save(scoreCap);
    }

    @Test
    void testUpdateScoreCapData_RecordNotFound() {
        // Arrange
        Long id = 1L;
        ScoreCapPutRequest request = new ScoreCapPutRequest();
        request.setValue(200);

        when(scoreCapRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = scoreCapService.updateScoreCapData(id, request);

        // Assert
        assertFalse(result);

        verify(scoreCapRepo, times(1)).findById(id);
        verify(scoreCapRepo, never()).save(any(ScoreCap.class));
    }

    @Test
    void testUpdateScoreCapData_ExceptionDuringUpdate() {
        // Arrange
        Long id = 1L;
        ScoreCapPutRequest request = new ScoreCapPutRequest();
        request.setValue(200);

        ScoreCap scoreCap = new ScoreCap();
        scoreCap.setId(id);
        scoreCap.setTotalRiskCappedScore(100);

        when(scoreCapRepo.findById(id)).thenReturn(Optional.of(scoreCap));
        when(scoreCapRepo.save(any(ScoreCap.class))).thenThrow(new RuntimeException("Database connection failed."));

        // Act
        boolean result = scoreCapService.updateScoreCapData(id, request);

        // Assert
        assertFalse(result);

        verify(scoreCapRepo, times(1)).findById(id);
        verify(scoreCapRepo, times(1)).save(scoreCap);
    }

}
