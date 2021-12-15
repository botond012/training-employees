package employees;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
