package com.usama.calculatorproblemv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class CalculatorProblemV2Application {

    public static void main(String[] args) {

	SpringApplication.run(CalculatorProblemV2Application.class, args);
    }
}
