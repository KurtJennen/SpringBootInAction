package be.ss.readinglist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReadingListApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServerWebTests {
	
	private static FirefoxDriver browser;
	
	@Value("${local.server.port}")
	private int port;

	@BeforeAll
	public static void openBrowser() {
		System.setProperty("webdriver.gecko.driver", "D:/In/geckodriver.exe");
		
		browser = new FirefoxDriver();
		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@AfterAll
	public static void closeBrowser() {
		browser.quit();
	}
	
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
	
	@Test()
	public void addBookToEMptyList() {
		String baseUrl = "http://localhost:" + port + "/craig";
		
		browser.get(baseUrl);
		
		assertEquals("You have no books in your book list", browser.findElementByTagName("div").getText());
		
		browser.findElementByName("title").sendKeys("BOOK TITLE");
		browser.findElementByName("author").sendKeys("BOOK AUTHOR");
		browser.findElementByName("isbn").sendKeys("1234567890");
		browser.findElementByName("description").sendKeys("DESCRIPTION");
		browser.findElementByTagName("form").submit();
		
		WebElement dl = browser.findElementByCssSelector("dt.bookHeadline");
		assertEquals("BOOK TITLE by BOOK AUTHOR (ISBN: 1234567890)", dl.getText());
		
		WebElement dt = browser.findElementByCssSelector("dd.bookDescription");
		assertEquals("DESCRIPTION", dt.getText());
	}
}
