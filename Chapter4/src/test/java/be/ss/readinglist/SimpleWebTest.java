package be.ss.readinglist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ReadingListApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT, value = {"server.port=8080"})
@SpringBootTest(classes = ReadingListApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SimpleWebTest {
	
	@Value("${local.server.port}")
	private int port;

	@Test()
	public void pageNotFound() {
		try {
			assertThrows(HttpClientErrorException.class, () -> {
				RestTemplate restTemplate = new RestTemplate();
//				restTemplate.getForObject("http://localhost:8000", String.class);
//				restTemplate.getForObject("http://localhost:8080", String.class);
				restTemplate.getForObject("http://localhost:{port}", String.class, port);
			});
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
			throw e;
		}
	}
}
