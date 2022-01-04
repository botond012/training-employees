package employees;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(value = AddressesGateway.class)
public class AddressesGatewayRestTemplateIT {
	@Autowired
	AddressesGateway addressesGateway;
	@Autowired
	MockRestServiceServer mockRestServiceServer;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	void testFindAddress() throws JsonProcessingException {
		String json = objectMapper.writeValueAsString(new AddressDto("Budapest", "Fő ut"));
		mockRestServiceServer.expect(requestTo(startsWith("http://192.168.126.129:8082/api/addresses")))
				.andExpect(queryParam("name", "Zoli"))
				.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

		AddressDto addressDto = addressesGateway.findAddressByName("Zoli");
		assertEquals("Budapest" ,addressDto.getCity());
		assertEquals("Fő ut" ,addressDto.getAddress());
	}
}
