package pl.agol.restserver;

/**
 * 
 * @author Andrzej Goławski
 * 
 */
public class RestServerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RestServerException(Throwable cause) {
		super(cause);
	}
}
