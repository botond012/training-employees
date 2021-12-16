package employees;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
@Data
@Repository
@AllArgsConstructor
public class EmployeeDao {

	private JdbcTemplate jdbcTemplate;

	private static Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		var id = rs.getLong("id");
		var name = rs.getString("emp_name");
		return new Employee(id, name);
	}

	public List<Employee> findALL() {
		return jdbcTemplate.query("select id, emp_name from employees", EmployeeDao::mapRow);
	}

	public Employee findById(long id) {
		return jdbcTemplate.queryForObject("select id, emp_name from employees where id = ?", EmployeeDao::mapRow, id);
	}

	public void createEmployee(Employee employee) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement("insert into employees (emp_name) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, employee.getName());
			return ps;
		}, keyHolder);

		employee.setId(keyHolder.getKey().longValue());
	}

	public void updateEmployee(Employee employee) {
		jdbcTemplate.update("update employees set emp_name = ? where id = ?", employee.getName(), employee.getId());
	}

	public void deleteById(long id) {
		jdbcTemplate.update("delete from employees where id = ?", id);
	}

	public void deleteAll() {
		jdbcTemplate.update("delete from employees");
	}

}
