package com.usama.calculatorproblemv2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usama.calculatorproblemv2.entity.ScoreCap;

public interface ScoreCapRepo extends JpaRepository<ScoreCap, Long> {

	
//    @Query("SELECT COALESCE((SELECT s.totalRiskCappedScore FROM ScoreCap s WHERE s.scoreCapCondition LIKE CONCAT('%', :condition, '%') AND s.countCondition = :counter), 100)")
    @Query("SELECT COALESCE((SELECT s.totalRiskCappedScore FROM ScoreCap s WHERE s.scoreCapCondition LIKE :condition AND s.countCondition = :counter), 100)")
	public Integer findTotalRiskCappedScore(String condition, Integer counter);
	
	
	
}
