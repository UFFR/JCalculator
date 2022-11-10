package util;

/**
 * {@link Printer}, but further abstracted.
 * @author UFFR
 *
 */
public interface IPrinter
{
	public default void printf(String format, Object...args)
	{
		print(String.format(format, args));
	}
	
	public default void printfln(String format, Object...args)
	{
		println(String.format(format, args));
	}
	
	/**
	 * Print a caught exception.
	 * @param cause The string that possibly caused it.
	 * @param e The exception thrown.
	 * @param printStack Whether or not to print the entire stack trace with the exception.
	 */
	public void printException(String cause, Exception e, boolean printStack);
	
	public void print(String string);
	public void print(Object object);
	public void print(char...chars);
	public void print(char c);
	public void println();
	
	public void println(String string);
	public void println(Object object);
	public void println(char...chars);
	public void println(char c);
}
