package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class VariableCalculator {
    public static void main(String[] args) {
        Map<String, String> expressions = new HashMap<>();
        expressions.put("abc_check", "12 + 3");
        expressions.put("xyz_check", "12 - 3");
        expressions.put("total_risk_score", "test_check +100 - xyz_check");
        expressions.put("test_check_1", "100");
        expressions.put("test_check", "abc_check + xyz_check");

        Map<String, Double> variables = evaluateVariables(expressions);

        for (Map.Entry<String, Double> entry : variables.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static Map<String, Double> evaluateVariables(Map<String, String> expressions) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Double> variables = new HashMap<>();

        // Construct the dependency graph
        for (Map.Entry<String, String> entry : expressions.entrySet()) {
            String variableName = entry.getKey();
            String expression = entry.getValue();
            List<String> dependentVariables = findDependentVariables(expression);
            graph.put(variableName, dependentVariables);
        }

        // Perform topological sort
        List<String> calculationOrder = topologicalSort(graph);

        // Calculate the values in the correct order
        System.out.println(calculationOrder);
        for (String variable : calculationOrder) {
            String expression = expressions.get(variable);
            double value = evaluateExpression(expression, variables);
            variables.put(variable, value);
        }

        return variables;
    }

    private static List<String> findDependentVariables(String expression) {
        List<String> dependentVariables = new ArrayList<>();
        String[] tokens = expression.split("\\+|-|\\*|/");
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty() && Character.isLetter(token.charAt(0))) {
                dependentVariables.add(token);
            }
        }
        return dependentVariables;
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        List<String> calculationOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String variable : graph.keySet()) {
            if (!visited.contains(variable)) {
                topologicalSortUtil(variable, graph, visited, recursionStack, calculationOrder);
            }
        }

//        Collections.reverse(calculationOrder);
        return calculationOrder;
    }

    private static void topologicalSortUtil(String variable, Map<String, List<String>> graph,
                                            Set<String> visited, Set<String> recursionStack, List<String> calculationOrder) {
        visited.add(variable);
        recursionStack.add(variable);
        List<String> dependents = graph.get(variable);
        if (dependents != null) {
            for (String dependent : dependents) {
                if (recursionStack.contains(dependent)) {
                    throw new RuntimeException("Circular dependency detected.");
                }
                if (!visited.contains(dependent)) {
                    topologicalSortUtil(dependent, graph, visited, recursionStack, calculationOrder);
                }
            }
        }
        recursionStack.remove(variable);
        calculationOrder.add(variable);
    }

    private static double evaluateExpression(String expression, Map<String, Double> variables) {
	
	System.out.println(expression+" "+variables);
	
        Expression exp = new ExpressionBuilder(expression).variables(variables.keySet()).build();

        // Set variable values
        for (Map.Entry<String, Double> entry : variables.entrySet()) {
            exp.setVariable(entry.getKey(), entry.getValue());
        }

        // Evaluate the expression
        return exp.evaluate();
    }
}