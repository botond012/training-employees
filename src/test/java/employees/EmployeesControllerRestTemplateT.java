package employees;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeesControllerRestTemplateT {
	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	EmployeesService employeesService;

	@RepeatedTest(2)
	void testListEmployees() throws Exception {
		employeesService.deleteAllEmployees();
		var employeeDto =
				testRestTemplate.postForObject("/api/employees", new CreateEmployeeCommand("Béla"), EmployeeDto.class);
		assertEquals("Béla", employeeDto.getName());
		testRestTemplate.postForObject("/api/employees", new CreateEmployeeCommand("Zoli"), EmployeeDto.class);
	var employees = 	testRestTemplate.exchange("/api/employees",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<EmployeeDto>>() {
				}).getBody();

		assertThat(employees).extracting(EmployeeDto::getName).containsExactly("Béla", "Zoli");
	}

}
