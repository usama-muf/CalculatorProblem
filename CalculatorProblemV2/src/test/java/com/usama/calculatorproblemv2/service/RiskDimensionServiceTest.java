package com.usama.calculatorproblemv2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.repo.RiskDimensionRepo;

//@RunWith(MockitoJUnitRunner.class)
public class RiskDimensionServiceTest {
    @Mock
    private RiskDimensionRepo riskDimensionRepo;

    @InjectMocks
    private RiskDimensionService riskDimensionService;

    @BeforeEach
    public void setup() {
	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNewRiskDimension_ValidInput_ReturnsUpdatedData() {
        RiskDimension request = new RiskDimension();
        request.setDimension("Dimension");
        request.setWeight(50);
        when(this.riskDimensionRepo.save(request)).thenReturn(request);

        RiskDimension result = riskDimensionService.createNewRiskDimension(request);

        assertEquals(request, result);
        verify(riskDimensionRepo, times(1)).save(request);
    }

//    @Test
//    public void testCreateNewRiskDimension_InvalidInput_ReturnsEmptyRiskDimension() {
//        RiskDimension request = new RiskDimension();
//        request.setDimension(null);
//        request.setWeight(-10);
//        RiskDimension result = riskDimensionService.createNewRiskDimension(request);
//        System.out.println(request);
//
//        assertEquals(new RiskDimension(null, -10), result);
//        verify(riskDimensionRepo, never()).save(any(RiskDimension.class));
//    }
}
