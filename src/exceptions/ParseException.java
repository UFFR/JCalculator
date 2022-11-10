package exceptions;

/**
 * When the parser encounters something unexpected. Usually goes to {@link SyntaxException} though.
 * @author UFFR
 *
 */
public class ParseException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4985563675489920570L;

	public ParseException(String message)
	{
		super(message);
	}

	public ParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
