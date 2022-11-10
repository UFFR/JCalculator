package exceptions;

/**
 * When a type doesn't support an operation with another type.
 * @author UFFR
 *
 */
public class TypeException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5884489233297757900L;

	public TypeException(String message)
	{
		super(message);
	}
	
	public TypeException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
