package employees;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.SocketUtils;

@RestClientTest(value = AddressesGateway.class)
public class AddressesGatewayWireMockIT {
	static final String host = "localhost";
	static int port = 8082;
	static WireMockServer wireMockServer;

	@BeforeAll
	static void startServer() {
		wireMockServer = new WireMockServer(wireMockConfig().port(port));
		WireMock.configureFor(host, port);
		wireMockServer.start();
	}

	@AfterAll
	static void stopServer() {
		wireMockServer.stop();

	}

	@BeforeEach
	void reset() {
		WireMock.reset();

	}


	@Test
	void test() throws JsonProcessingException {

		String resource = "/api/addresses";
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(new AddressDto("Budapest", "Fő"));

		stubFor(get(urlPathEqualTo(resource))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody(json)));

		AddressesGateway addressesGateway = new AddressesGateway(new RestTemplateBuilder());
		AddressDto addressDto = addressesGateway.findAddressByName("Bela");

		verify(getRequestedFor(urlPathEqualTo(resource)).withQueryParam("name", equalTo("Bela")));
		assertThat(addressDto.getAddress()).isEqualTo("Fő");
		assertThat(addressDto.getCity()).isEqualTo("Budapest");
	}


}
