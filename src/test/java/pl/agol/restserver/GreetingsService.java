package pl.agol.restserver;

import javax.inject.Named;

/**
 * 
 * @author Andrzej Goławski
 */
@Named
public class GreetingsService {

	public static final String GREETINGS_MESSAGE = "hello world!";

	public String getGreetings() {
		return GREETINGS_MESSAGE;
	}
}
