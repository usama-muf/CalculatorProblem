package com.usama.calculatorproblemv2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.usama.calculatorproblemv2.dto.CompanyRiskScoreRequest;
import com.usama.calculatorproblemv2.entity.CompanyParameterScore;
import com.usama.calculatorproblemv2.entity.RiskParameters;
import com.usama.calculatorproblemv2.service.CompanyParameterScoreService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CompanyParameterScoreController.class)
@WithMockUser

public class CompanyParameterScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyParameterScoreService companyRiskScoreService;

    private Map<String, Integer> paramsMap;

    String exampleRiskScoreJsonString = "";

    Set<RiskParameters> setParams = new HashSet<>();

    CompanyParameterScore companyRiskScore = null;

    public CompanyParameterScoreControllerTest() {
	setParams.add(new RiskParameters("params1", 1));
	setParams.add(new RiskParameters("params2", 2));
	setParams.add(new RiskParameters("params3", 3));
	setParams.add(new RiskParameters("params4", 4));

	companyRiskScore = new CompanyParameterScore("company 1", setParams);
	
	exampleRiskScoreJsonString = "{\n" + "  \"companyName\": \"TCSSS\",\n" + "  \"parameters\": {\n"
		+ "    \"Information Security\": 94,\n" + "    \"Resilience\": 12,\n" + "    \"Conduct\": 70,\n"
		+ "    \"Respect\": 50\n" + "  }\n" + "}";
    }

    @Test
    public void createCompanyRiskScoreTest() throws Exception {
	String success = "Table created successfully.";

	paramsMap = new HashMap<>();
	paramsMap.put("params 1", 1);
	paramsMap.put("params 2", 2);
	paramsMap.put("params 3", 3);

	CompanyRiskScoreRequest riskScoreRequest = new CompanyRiskScoreRequest();
	riskScoreRequest.setCompanyName("company 1");
	riskScoreRequest.setParameters(paramsMap);

	Mockito.when(companyRiskScoreService.createNewRiskScoreRow(Mockito.any(CompanyRiskScoreRequest.class)))
		.thenReturn(success);

	// Send course as body to /v1/api/crs/create
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/api/crs/create")
		.accept(MediaType.APPLICATION_JSON).content(exampleRiskScoreJsonString)
		.contentType(MediaType.APPLICATION_JSON);

	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

	MockHttpServletResponse response = result.getResponse();

	assertEquals(HttpStatus.CREATED.value(), response.getStatus());

	assertEquals("http://localhost/v1/api/crs/create", response.getHeader(HttpHeaders.LOCATION));

    }

    @Test
    public void getAllCompanyDataTest() throws Exception {

	List<CompanyParameterScore> listRiskScores = Arrays.asList(companyRiskScore);

	Mockito.when(companyRiskScoreService.findAll()).thenReturn(listRiskScores);

	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/api/crs/getall")
		.accept(MediaType.APPLICATION_JSON);

	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

	System.out.println(result.getResponse().getContentAsString());
	String expected = "[{\n" + "  \"companyName\": \"company 1\",\n" + "  \"parameters\": [\n" + "    {\n"
		+ "      \"parameterName\": \"params1\",\n" + "      \"parameterValue\": 1\n" + "    },\n" + "    {\n"
		+ "      \"parameterName\": \"params2\",\n" + "      \"parameterValue\": 2\n" + "    },\n" + "    {\n"
		+ "      \"parameterName\": \"params3\",\n" + "      \"parameterValue\": 3\n" + "    },\n" + "    {\n"
		+ "      \"parameterName\": \"params4\",\n" + "      \"parameterValue\": 4\n" + "    }\n" + "  ]\n"
		+ "}]";

	System.out.println(expected);
	JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void findByCompanyNameBaseTest() throws Exception{
	
	Mockito.when(companyRiskScoreService.findByCompanyName(Mockito.anyString())).thenReturn(companyRiskScore);
	
	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/api/crs/get/company 1").accept(MediaType.APPLICATION_JSON);
	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
	String expected = "{\n" + "  \"companyName\": \"company 1\",\n" + "  \"parameters\": [\n" + "    {\n"
		+ "      \"parameterName\": \"params1\",\n" + "      \"parameterValue\": 1\n" + "    },\n" + "    {\n"
		+ "      \"parameterName\": \"params2\",\n" + "      \"parameterValue\": 2\n" + "    },\n" + "    {\n"
		+ "      \"parameterName\": \"params3\",\n" + "      \"parameterValue\": 3\n" + "    },\n" + "    {\n"
		+ "      \"parameterName\": \"params4\",\n" + "      \"parameterValue\": 4\n" + "    }\n" + "  ]\n"
		+ "}";
	
	JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

}
