package employees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

@Service
public class EmployeesService {

	private ModelMapper modelMapper;
	private AtomicLong idGenerator = new AtomicLong();

	public EmployeesService(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	private List<Employee> employees = Collections.synchronizedList(new ArrayList<>(List.of(
			new Employee(idGenerator.incrementAndGet(), "Béla"), new Employee(idGenerator.incrementAndGet(), "Lajos")
	)));


	public List<EmployeeDto> listEmployees(Optional<String> prefix) {
		var targetListType = new TypeToken<List<EmployeeDto>>() {
		}.getType();
		var filteredEmployees = employees.stream()
				.filter(employee -> prefix.isEmpty() || employee.getName().toLowerCase().startsWith(prefix.get().toLowerCase()))
				.collect(Collectors.toList());
		return modelMapper.map(filteredEmployees, targetListType);
	}

	public EmployeeDto listEmployeeById(long id) {
		return modelMapper.map(employees.stream()
						.filter(e -> id == e.getId())
						.findFirst()
						.orElseThrow(() -> new EmployeesNotFoundException(id)),
				EmployeeDto.class);
	}

	public EmployeeDto createEmployee(CreateEmployeeCommand command) {
		var employee = new Employee(idGenerator.incrementAndGet(), command.getName());
		employees.add(employee);
		return modelMapper.map(employee, EmployeeDto.class);
	}

	public EmployeeDto updateEmployee(long id, UpdateEmployeeCommand command) {
		var employee = employees.stream()
				.filter(e -> id == e.getId())
				.findFirst()
				.orElseThrow(() -> new EmployeesNotFoundException(id));
		employee.setName(command.getName());
		return modelMapper.map(employee, EmployeeDto.class);
	}

	public void deleteEmployee(long id) {
		var employee = employees.stream()
				.filter(e -> id == e.getId())
				.findFirst()
				.orElseThrow(() ->new EmployeesNotFoundException(id));
employees.remove(employee);
	}
}
