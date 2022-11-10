package exceptions;

/**
 * Handling matrices with incongruent dimensions or otherwise invalid.
 * @author UFFR
 *
 */
public class DimensionException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6158425389917695418L;

	public DimensionException(String message)
	{
		super(message);
	}
	
	public DimensionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
