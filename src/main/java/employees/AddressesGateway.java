package employees;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AddressesGateway {
	public AddressesGateway(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	private final RestTemplate restTemplate;

	public AddressDto findAddressByName(String name){
		return restTemplate.getForObject("http://192.168.126.129:8082/api/addresses?name={name}", AddressDto.class, name);
	}

}
