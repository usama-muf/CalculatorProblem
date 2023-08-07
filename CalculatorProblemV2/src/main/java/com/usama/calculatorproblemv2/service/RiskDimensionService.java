package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.RiskDimensionResponse;
import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.repo.RiskDimensionRepo;

@Service
public class RiskDimensionService {

    private final RiskDimensionRepo riskDimensionRepo;

    Logger log = LoggingUtils.logger;

    private static boolean validateRiskDimension(RiskDimension riskDimension) {
	// Validate dimension and weight
	if (riskDimension.getDimension() == null || riskDimension.getDimension().isEmpty()) {
	    return false;
	}
	if (riskDimension.getWeight() == null || riskDimension.getWeight() <= 0 || riskDimension.getWeight() > 100) {
	    return false;
	}
	return true;
    }

    public RiskDimensionService(RiskDimensionRepo riskDimensionRepo) {
	this.riskDimensionRepo = riskDimensionRepo;
    }

    public List<String> getAllDimensionsName() {
	try {
	    List<RiskDimension> data = riskDimensionRepo.findAll();
	    List<String> dimensions = data.stream().map(d -> d.getDimension()).collect(Collectors.toList());
	    log.info("Fetching all the Dimensions from Risk_Dimension tbl");
	    return dimensions;

	} catch (Exception e) {
	    log.error("Error fetching Dimensions from Risk_Dimension tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}

    }

    public List<RiskDimension> getAllDimensionsAndWeight() {
	try {
	    List<RiskDimension> data = riskDimensionRepo.findAll();
	    log.info("Fetching all the Data from Risk_Dimension tbl");
	    return data;

	} catch (Exception e) {
	    log.error("Error fetching Data from Risk_Dimension tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }

    public void deleteByDimensionName(String dimensionName) {

	try {

	    RiskDimension riskDimension = this.riskDimensionRepo.findById(dimensionName)
		    .orElseThrow(() -> new RuntimeException(dimensionName + " : NOT FOUND"));
	    this.riskDimensionRepo.delete(riskDimension);
	    log.info("Successfully DELETED from the Risk_Dimension tbl with dimensionName: {}", dimensionName);

	} catch (Exception e) {
	    log.error("Failed to delete from Risk_Dimension tbl with dimensionName: {}", dimensionName);
	    log.error(e.getMessage());
	}

    }

    public RiskDimensionResponse updateRiskDimension(String dimensionName, RiskDimension data) {

	RiskDimensionResponse response = new RiskDimensionResponse();

	try {

	    if (!validateRiskDimension(data)) {
		response.setValid(false);
		response.setMessage("Invalid risk dimension");
		return response;
	    }

	    RiskDimension riskDimension = this.riskDimensionRepo.findById(dimensionName)
		    .orElseThrow(() -> new RuntimeException(dimensionName + " : NOT FOUND"));

	    // checking if adding this new weight in our tbl makes the sum > 100
	    Integer dimensionWeightSum = dimensionWeightSum(data);

	    if (dimensionWeightSum == null) {
		response.setValid(false);
		response.setMessage("Sum of Weights cannot be NULL");
		return response;
	    } else if (dimensionWeightSum < 0) {
		response.setValid(false);
		response.setMessage("Sum of Weights cannot be less than 0%, Please update to make it 100%");
		return response;
	    } else if (dimensionWeightSum > 100) {
		response.setValid(false);
		response.setMessage("Sum of Weights cannot be greater than 100%, Please update dimensions to make it 100%");
		return response;
	    }

	    riskDimension.setWeight(data.getWeight());
	    RiskDimension updatedData = this.riskDimensionRepo.save(riskDimension);
	    log.info("Successfully Updated in the Risk_Dimension tbl with dimensionName: {} and body : {}",
		    dimensionName, data);

	    response.setValid(true);

	    if (dimensionWeightSum != 100) {
		response.setMessage("Sum of Weights Should be 100% ");
		return response;
	    } else if (dimensionWeightSum == 100) {
		response.setMessage("Updated Successfull !!");
		return response;
	    }
	    response.setMessage("Updated Successfull !!");
	    return response;

	} catch (Exception e) {
	    log.error("Failed to Update in Risk_Dimension tbl with dimensionName: {} and body: {}", dimensionName,
		    data);
	    log.error(e.getMessage());

	    response.setValid(false);
	    response.setMessage("Server Error!! Something went wrong ");
	    return response;
	}

    }

    /**
     * @param riskDimension // the dimension wheich we have to update.
     */
    private Integer dimensionWeightSum(RiskDimension riskDimension) {
	int dimensionWeightSum = this.riskDimensionRepo.findAll().stream().mapToInt(dimension -> {
	    if (dimension.getDimension().equalsIgnoreCase(riskDimension.getDimension()))
		return riskDimension.getWeight();
	    else
		return dimension.getWeight();
	}).sum();
	return dimensionWeightSum;
    }

    public RiskDimension createNewRiskDimension(RiskDimension request) {

	try {

	    RiskDimension updatedData = this.riskDimensionRepo.save(request);
	    log.info("Successfully Created in the Risk_Dimension tbl with body : {}", request);
	    return updatedData;

	} catch (Exception e) {
	    log.error("Failed to Create in Risk_Dimension tbl with body: {}", request);
	    log.error(e.getMessage());
	    return new RiskDimension();
	}

    }

}
