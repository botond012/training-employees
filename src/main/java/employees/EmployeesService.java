package employees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

@Service
public class EmployeesService {

	private ModelMapper modelMapper;

	public EmployeesService(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	private List<Employee> employees = Collections.synchronizedList(new ArrayList<>(List.of(
			new Employee(1L, "Béla"), new Employee(2L, "Lajos")
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
						.orElseThrow(() -> new IllegalArgumentException(String.format("Employee with id:%s not found", id))),
				EmployeeDto.class);
	}
}
