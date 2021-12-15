package employees;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

	private EmployeesService employeesService;

	public EmployeesController(EmployeesService employeesService) {
		this.employeesService = employeesService;
	}


	@GetMapping
	public List<EmployeeDto> listEmployees(@RequestParam Optional<String> prefix) {
		return employeesService.listEmployees(prefix);
	}

	@GetMapping("/{id}")
	public EmployeeDto findEmployeeById(@PathVariable("id") long id) {
		return employeesService.listEmployeeById(id);
	}

	//	@GetMapping("/{id}")
	//	public ResponseEntity findEmployeeById(@PathVariable("id") long id) {
	//		try {
	//			return ResponseEntity.ok(employeesService.listEmployeeById(id));
	//		} catch (IllegalArgumentException e) {
	//			return ResponseEntity.notFound().build();
	//		}
	//
	//	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeDto createEmployee(@RequestBody CreateEmployeeCommand command) {
		return employeesService.createEmployee(command);
	}

	@PutMapping("/{id}")
	public EmployeeDto createEmployee(@PathVariable("id") long id, @RequestBody UpdateEmployeeCommand command) {
		return employeesService.updateEmployee(id, command);
	}


	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEmployee(@PathVariable("id") long id) {
		employeesService.deleteEmployee(id);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Problem> handleNotFound(IllegalArgumentException e) {
		var problem = Problem.builder()
				.withType(URI.create("employees/not-found"))
				.withTitle("Not Found")
				.withStatus(Status.NOT_FOUND)
				.withDetail(e.getMessage()).build();
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(problem);
	}


}
