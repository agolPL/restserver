package pl.agol.restserver;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * 
 * @author Andrzej Goławski
 * 
 */
@Named
@Path("testService")
public class TestService {

	@Inject
	private GreetingsService greetingsService;

	@GET
	@Path("showGreetings")
	public String showGreetings() {
		return greetingsService.getGreetings();
	}
}
