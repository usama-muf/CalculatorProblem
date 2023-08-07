package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.ScoreCapPutRequest;
import com.usama.calculatorproblemv2.entity.ScoreCap;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;

@Service
public class ScoreCapService {

    private final ScoreCapRepo scoreCapRepo;
    Logger log = LoggingUtils.logger;
    
    public ScoreCapService(ScoreCapRepo scoreCapRepo) {
	this.scoreCapRepo = scoreCapRepo;
    }


    public ScoreCap createNewScoreCap(ScoreCap cap) {
	if (cap == null) {
	    return null;
	}

	if (cap.getCountCondition() < 0 || cap.getTotalRiskCappedScore() < 0) {
	    return null;
	}

	if (cap.getCountCondition() > 100 || cap.getTotalRiskCappedScore() > 100) {
	    return null;
	}

	if (cap.getScoreCapCondition() == null || cap.getScoreCapCondition().trim().isEmpty()) {
	    return null;
	}

	try {
	    log.info("Successfully Saving in Score_Cap table with body {}", cap);
	    return this.scoreCapRepo.save(cap);
	} catch (Exception e) {
	    return null;
	}
    }

    public List<ScoreCap> getAllScoreCap() {
	try {	    
	    return this.scoreCapRepo.findAll();
	} catch (Exception e) {
	    return new ArrayList<>();
	}
    }

    public Optional<ScoreCap> getScoreCapById(@PathVariable Long id) {
	Optional<ScoreCap> data = this.scoreCapRepo.findById(id);

	if (data.isPresent())
	    return data;

	return null;
    }


    public boolean deleteScoreCapData(Long id) {
	if (id == null) {
	    log.error("Invalid ID provided: null");
	    return false;
	}

	try {
	    // Check if the provided ID can be parsed to Long
	    Long.parseLong(String.valueOf(id));
	} catch (NumberFormatException e) {
	    log.error("Invalid ID format provided: {}", id);
	    return false; // Return false immediately if the ID is not a valid Long
	}

	try {
	    Optional<ScoreCap> findById = this.scoreCapRepo.findById(id);

	    if (findById.isPresent()) {
		this.scoreCapRepo.delete(findById.get());
		log.info("Successfully DELETED from the Score_Cap tbl with id: {}", id);
		return true;
	    } else {
		log.error("Failed to delete from Score_Cap tbl with id: {}", id);
		return false;
	    }
	} catch (Exception e) {
	    log.error("Failed to delete from Score_Cap tbl with id: {}, Something went wrong", id);
	    log.error(e.getMessage());
	    return false;
	}
    }

    public boolean updateScoreCapData(Long id, ScoreCapPutRequest value) {
	try {
	    Optional<ScoreCap> findById = this.scoreCapRepo.findById(id);

	    if (findById.isPresent()) {
		findById.get().setTotalRiskCappedScore(value.getValue());
		this.scoreCapRepo.save(findById.get());
		log.info("Successfully Updated in the Score_Cap tbl with id: {}", id);
		return true;
	    } else
		log.error("Failed to delete from Score_Cap tbl with id: {}", id);

	} catch (Exception e) {
	    log.error("Failed to delete from Score_Cap tbl with id: {}, Something went wrong", id);
	    log.error(e.getMessage());
	}
	return false;
    }

}
