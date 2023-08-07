package com.usama.calculatorproblemv2.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usama.calculatorproblemv2.entity.ScoreCap;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;

@RestController
@RequestMapping("/v1/api/sc")
public class ScoreCapController {

	private final ScoreCapRepo scoreCapRepo;

	public ScoreCapController(ScoreCapRepo scoreCapRepo) {
		this.scoreCapRepo = scoreCapRepo;
	}

	
	@PostMapping("/create")
	public ScoreCap createNewScoreCap(@RequestBody ScoreCap cap) {
		return this.scoreCapRepo.save(cap);
	}
	
	@GetMapping("/getall")
	public List<ScoreCap> getAllScoreCap() {
		return this.scoreCapRepo.findAll();
	}
	
	
	
}
