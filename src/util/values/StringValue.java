package util.values;

import java.util.Objects;

import exceptions.TypeException;
import main.Main;
import util.CompareType;

/**
 * A value wrapper around a string. Does not support most operations.
 * @author UFFR
 *
 */
public class StringValue implements Value<String>, CharSequence
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3432618824086205688L;
	private final String text;
	
	public StringValue(String text)
	{
		this.text = text;
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.VALUE;
	}
	
	@Override
	public int hashCode()
	{
		return text.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		final String toTest;
		if (obj instanceof StringValue)
			toTest = ((StringValue) obj).text;
		else if (obj instanceof String)
			toTest = (String) obj;
		else
			return false;
		return Objects.equals(text, toTest);
	}

	@Override
	public String toString()
	{
		return '"' + text + '"';
	}

	@Override
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		return new StringValue(text + augend);
	}

	@Override
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public Value<?> round() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}
	
	@Override
	public Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("String value types do not support this operation type.");
	}

	@Override
	public boolean compare(Value<?> value, CompareType type, boolean printOut) throws UnsupportedOperationException, TypeException
	{
		final String toCompare;
		if (value instanceof StringValue)
			toCompare = ((StringValue) value).text;
		else
			throw new TypeException("Value " + value + " cannot be used for this operation within this context.");
		final boolean result;
		switch (type)
		{
			case EQUALS: result = equals(value); break;
			case GREATER_THAN: result = text.compareTo(toCompare) > 0; break;
			case GREATER_THAN_OR_EQUAL: result = text.compareTo(toCompare) >= 0; break;
			case LESS_THAN: result = text.compareTo(toCompare) < 0; break;
			case LESS_THAN_OR_EQUAL: result = text.compareTo(toCompare) <= 0; break;
			case NOT_EQUALS: result = text.compareTo(toCompare) != 0; break;
			default: result = false; break;
		}
		if (printOut)
			Main.getPrinter().println(result);
		return result;
	}

	@Override
	public String getValue()
	{
		return text;
	}
	
	@Override
	public char charAt(int index)
	{
		return text.charAt(index);
	}

	@Override
	public int length()
	{
		return text.length();
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return text.subSequence(start, end);
	}

}
