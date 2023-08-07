package com.usama.calculatorproblemv2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usama.calculatorproblemv2.entity.RiskScoreLevel;

public interface RiskScoreLevelRepo extends JpaRepository<RiskScoreLevel, Long> {

//	@Query("SELECT s.level FROM RiskScoreLevel s WHERE :score >= CAST(SUBSTRING_INDEX(s.score, '-', 1) AS SIGNED) AND :score <= CAST(SUBSTRING_INDEX(s.score, '-', -1) AS SIGNED)")
//	@Query("SELECT 42 >= CONVERT(SUBSTRING_INDEX('01-20', '-', 1) AS DECIMAL) AND 42 <= CONVERT(SUBSTRING_INDEX('01-20', '-', -1) AS DECIMAL)")
 
//	@Query("SELECT s.level "
//			+ "FROM RiskScoreLevel s "
//			+ "WHERE :score >= FUNCTION('CAST', FUNCTION('SUBSTRING_INDEX', s.score, '-', 1) AS SIGNED) "
//			+ "AND   :score <= FUNCTION('CAST', FUNCTION('SUBSTRING_INDEX', s.score, '-', -1) AS SIGNED)")
//  
	
    @Query(value = "SELECT s.level FROM RiskScoreLevel s WHERE :score >= CAST(SUBSTRING_INDEX(s.score, '-', 1) AS INTEGER) AND :score <= CAST(SUBSTRING_INDEX(s.score, '-', -1) AS INTEGER)")
	String findLevelByScore(int score);
    
	
	
}
