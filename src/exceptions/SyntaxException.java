package exceptions;

/**
 * When invalid or malformed tokens/values are encountered by any structure, usually the Parser.
 * @author UFFR
 *
 */
public class SyntaxException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3666234120068943185L;

	public SyntaxException(String message)
	{
		super(message);
	}
	
	public SyntaxException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
