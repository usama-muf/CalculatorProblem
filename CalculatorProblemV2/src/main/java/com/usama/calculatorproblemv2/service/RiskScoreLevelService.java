package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.RiskScoreLevelDto;
import com.usama.calculatorproblemv2.dto.RiskScoreLevelPutDto;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.entity.RiskScoreLevel;
import com.usama.calculatorproblemv2.repo.RiskScoreLevelRepo;

@Service
public class RiskScoreLevelService {

    private final RiskScoreLevelRepo riskScoreLevelRepo;
    Logger log = LoggingUtils.logger;

    public RiskScoreLevelService(RiskScoreLevelRepo riskScoreLevelRepo) {
	super();
	this.riskScoreLevelRepo = riskScoreLevelRepo;
    }

    public List<RiskScoreLevel> getAllRiskScoreLevels() {
	try {
	    List<RiskScoreLevel> data = riskScoreLevelRepo.findAll();
	    log.info("Fetching all the Data from Risk_Score_Level tbl");
	    return data;

	} catch (Exception e) {
	    log.error("Error fetching Data from Risk_Score_Level tbl");
	    log.error(e.getMessage());
	}
	return new ArrayList<>();
    }

    public RiskScoreLevel getRiskScoreLevel(String levelName) {
	RiskScoreLevel data = null;
	try {
	    data = riskScoreLevelRepo.findByLevel(levelName);
	    log.info("Fetching all the Data from Risk_Score_Level tbl");

	} catch (Exception e) {
	    log.error("Error fetching Data from Risk_Score_Level tbl");
	    log.error(e.getMessage());
	}
	return data;
    }

    public void createNewRiskScoreLevel(RiskScoreLevelDto request) {

	try {
	    RiskScoreLevel updatedData = dtoToEntity(request);
	    this.riskScoreLevelRepo.save(updatedData);
	    log.info("Successfully Created in the Risk_Score_Level tbl with body : {}", request);

	} catch (Exception e) {
	    log.error("Failed to Create in Risk_Score_Level tbl with body: {}", request);
	    log.error(e.getMessage());
	}
    }

    // return true if "isNotInterfering"
    public boolean updateRiskScoreLevel(String levelName, RiskScoreLevelPutDto scoreDto) {

	// fetch by levelName
	RiskScoreLevel data = this.riskScoreLevelRepo.findByLevel(levelName);

	// Iterating over riskScoreLevel data and filtering by levelName
	List<RiskScoreLevel> updatedData = this.riskScoreLevelRepo.findAll().stream()
		.filter(riskScore -> !riskScore.getLevel().equalsIgnoreCase(levelName)).collect(Collectors.toList());

	boolean isValid = checkScoreValid(scoreDto.getMinScore(), scoreDto.getMaxScore(), levelName, updatedData);

	if (!isValid)
	    return false;

	data.setMinScore(scoreDto.getMinScore());
	data.setMaxScore(scoreDto.getMaxScore());

	log.info("Successfully Updated in the Risk_Score_Level tbl with LevelName: {} and body : {}", levelName, data);
	this.riskScoreLevelRepo.save(data);
	return true;

    }

    public boolean deleteByLevelName(String levelName) {
	try {

	    RiskScoreLevel data = this.riskScoreLevelRepo.findByLevel(levelName);
	    if (data != null) {
		this.riskScoreLevelRepo.delete(data);
		log.info("Successfully DELETED from the Risk_Score_Level tbl with levelName: {}", levelName);
		return true;
	    } else
		log.error("Failed to delete from Risk_Score_Level tbl with levelName: {}", levelName);

	} catch (Exception e) {
	    log.error("Failed to delete from Risk_Score_Level tbl with LevelName: {}", levelName);
	    log.error(e.getMessage());
	}
	return false;

    }

    private RiskScoreLevel dtoToEntity(RiskScoreLevelDto dto) {
	RiskScoreLevel entity = new RiskScoreLevel();
	entity.setMinScore(dto.getMinScore());
	entity.setMaxScore(dto.getMaxScore());
	entity.setLevel(dto.getLevel());
	return entity;
    }

    public boolean checkScoreValid(int min, int max, String levelName) {

	List<RiskScoreLevel> allRiskScoreLevels = getAllRiskScoreLevels();
	return checkScoreValid(min, max, levelName, allRiskScoreLevels);

    }

    public boolean checkScoreValid(int min, int max, String levelName, List<RiskScoreLevel> data) {
	if (min > max || min <= 0 || max <= 0 || min > 100 || max > 100)
	    return false;

	log.info(data);

	if (data.size() == 0)
	    return true;

	for (RiskScoreLevel level : data) {
	    Integer mm1 = level.getMinScore();
	    Integer mx1 = level.getMaxScore();

	    log.info("value {}, {}, {}, {}, {}", mm1, mx1, min, max, level);
	    if (levelName.equalsIgnoreCase(level.getLevel()))
		return false;

	    if (mm1 < min) {
		if (mx1 >= min) {
		    return false;
		}
//		return false;
	    }

	    if (mm1 > min) {
		if (max >= mm1) {
		    return false;
		}
//		return false;
	    }
	    if( mm1 == min) return false;
//	    if (mm1 < min) {
//		if (mx1 < min) {
//		    return true;
//		}
//		return false;
//	    }
//	    
//	    if (mm1 > min) {
//		if (max < mm1) {
//		    return true;
//		}
//		return false;
//	    }
	}

	return true;

    }

    public List<String> getAllUniqueLevelName() {

	try {
	    List<String> data = riskScoreLevelRepo.findAll().stream().map(levels -> levels.getLevel())
		    .collect(Collectors.toList());
	    log.info("Fetching Unique LevelName from Risk_Score_Level tbl");
	    return data;

	} catch (Exception e) {
	    log.error("Error fetching Data from Risk_Score_Level tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }

    public String getLevelNamefromValue(Integer value) {

	List<String> levelNames = this.riskScoreLevelRepo.findAll().stream()
		.filter(data -> value >= data.getMinScore() && value <= data.getMaxScore()).map(data -> data.getLevel()).collect(Collectors.toList());

	if(!levelNames.isEmpty()) 
	    return levelNames.get(0);
	
	else {
	    return null;
	}
    }

}
