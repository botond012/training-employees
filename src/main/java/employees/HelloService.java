package employees;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

	public String sayHello(){
		return "hello from serviceas2123123 " + LocalDateTime.now();
	}
}
