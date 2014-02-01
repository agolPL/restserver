package pl.agol.restserver;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.testng.annotations.Test;


/**
 * 
 * @author Andrzej Goławski
 * 
 */
public class RestServerTest {
	
	private static final String HOST = "localhost";
	private static final int PORT = 8181;
	private static final String CONTEXT_PATH = "/restserver";
	private static final String  SPRING_CTX_CFG_LOCATION = "/src/test/resources/beans.xml";

	@Test
	public void should_create_rest_server() {

		RestServer restServer = buildRestserver();
		
		assertNotNull(restServer);
		assertEquals(restServer.getHost(), HOST);
		assertEquals(restServer.getPort(), PORT);
		assertEquals(restServer.getContextPath(), CONTEXT_PATH);
		assertEquals(restServer.getSpringContextConfigLocation(), SPRING_CTX_CFG_LOCATION);
	}
	
	private RestServer buildRestserver() {
		return RestServer.builder()
				.setHost(HOST)
				.setPort(PORT)
				.setContextPath(CONTEXT_PATH)
				.setSpringContextConfigLocation(SPRING_CTX_CFG_LOCATION)
				.build();
	}
	
	@Test
	public void should_receive_greetings() throws Exception {
		
		RestServer restServer = buildRestserver();
		try {
			restServer.start();
			String message = readMessageFromURL();
			
			assertEquals(message, GreetingsService.GREETINGS_MESSAGE);
			
		} finally {
			restServer.stop();
		}
	}
	
	private String readMessageFromURL() throws IOException {
		
		URL url = new URL("http://localhost:8181/restserver/testService/showGreetings");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		return reader.readLine();
	}
	
}
