package employees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeesService {

	private ModelMapper modelMapper;
	private EmployeeDao employeeDao;


	public List<EmployeeDto> listEmployees(Optional<String> prefix) {
		var targetListType = new TypeToken<List<EmployeeDto>>() {
		}.getType();
		var filteredEmployees = employeeDao.findALL().stream()
				.filter(employee -> prefix.isEmpty() || employee.getName().toLowerCase().startsWith(prefix.get().toLowerCase()))
				.collect(Collectors.toList());
		return modelMapper.map(filteredEmployees, targetListType);
	}

	public EmployeeDto listEmployeeById(long id) {
		return modelMapper.map(employeeDao.findById(id), EmployeeDto.class);
	}

	public EmployeeDto createEmployee(CreateEmployeeCommand command) {
		var employee = new Employee(command.getName());
		employeeDao.createEmployee(employee);
		log.info("Employee has been created");
		log.debug(String.format("Employee has been created, with name: %s id: %s", employee.getName(), employee.getId()));
		return modelMapper.map(employee, EmployeeDto.class);
	}

	public EmployeeDto updateEmployee(long id, UpdateEmployeeCommand command) {
		var employee = new Employee(id,command.getName());
		employeeDao.updateEmployee(employee);
		return modelMapper.map(employee, EmployeeDto.class);
	}

	public void deleteEmployee(long id) {
		employeeDao.deleteById(id);
	}

	public void deleteAllEmployees() {
		employeeDao.deleteAll();
	}


}
