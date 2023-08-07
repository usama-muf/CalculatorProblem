package com.usama.calculatorproblemv2.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usama.calculatorproblemv2.entity.Output;

public interface OutputRepo extends JpaRepository<Output, String> {

}
