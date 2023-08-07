package com.usama.calculatorproblemv2.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.ScoreCapPutRequest;
import com.usama.calculatorproblemv2.entity.ScoreCap;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;
import com.usama.calculatorproblemv2.service.ScoreCapService;

@RestController
@RequestMapping("/v1/api/sc")
@Validated

public class ScoreCapController {

    private final ScoreCapService scoreCapService;

    Logger log = LoggingUtils.logger;

    public ScoreCapController(ScoreCapService scoreCapService) {
	super();
	this.scoreCapService = scoreCapService;
    }

    @PostMapping("/create")
    public ScoreCap createNewScoreCap(@RequestBody ScoreCap cap) {
	return this.scoreCapService.createNewScoreCap(cap);
    }

    @GetMapping("/getall")
    public List<ScoreCap> getAllScoreCap() {
	return this.scoreCapService.getAllScoreCap();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ScoreCap> getScoreCapById(@PathVariable Long id) {
	Optional<ScoreCap> data = this.scoreCapService.getScoreCapById(id);

	if (data.isPresent())
	    return ResponseEntity.ok(data.get());

	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateScoreCapData(@PathVariable Long id, @RequestBody ScoreCapPutRequest value) {

	log.info("Received Put request for Score_Cap tbl for id: {} with body: {}", id, value);

	try {
	    boolean result = this.scoreCapService.updateScoreCapData(id, value);

	    if (result) {
		return ResponseEntity.ok().build();
	    } else {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	} catch (Exception e) {
	    log.error("Unable to delete from Risk_Level with id: {}", id);
	    log.error(e.getMessage());
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRiskScoreLevel(@PathVariable Long id) {
	log.info("Received DELETE request for Score_Cap tbl with id: {}", id);

	try {
	    boolean result = this.scoreCapService.deleteScoreCapData(id);

	    if (result) {
		return ResponseEntity.ok().build();
	    } else {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	} catch (Exception e) {
	    log.error("Unable to delete from Risk_Level with id: {}", id);
	    log.error(e.getMessage());
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

    }

}
