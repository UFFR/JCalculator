package util.values;

import exceptions.TypeException;
import main.Main;
import util.CompareType;

/**
 * A value that wraps a {@code boolean}. Doesn't support many operations.
 * @author UFFR
 *
 */
public class BooleanValue implements Value<Boolean>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 267121130467320726L;
	private final boolean bool;
	
	public BooleanValue(boolean bool)
	{
		this.bool = bool;
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.TEXT;
	}
	
	@Override
	public int hashCode()
	{
		return Boolean.hashCode(bool);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		final boolean other;
		if (obj instanceof BooleanValue)
			other = ((BooleanValue) obj).bool;
		else if (obj instanceof Boolean)
			other = ((Boolean) obj).booleanValue();
		else
			return false;
		return bool == other;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(bool);
	}
	
	@Override
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		return new BooleanValue(!bool);
	}

	@Override
	public Value<?> round() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}
	
	@Override
	public Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		throw new UnsupportedOperationException("Boolean value types do not support this operation type.");
	}

	@Override
	public boolean compare(Value<?> value, CompareType type, boolean printOut)
			throws UnsupportedOperationException, TypeException
	{
		if (value instanceof BooleanValue)
		{
			final boolean other = ((BooleanValue) value).bool, result;
			switch (type)
			{
				case EQUALS: result = bool == other; break;
				case NOT_EQUALS: result = bool != other; break;
				default: throw new UnsupportedOperationException("Boolean value types do not support the comparison operator: '" + type.symbol + "'.");
			}
			if (printOut)
				Main.getPrinter().println(result);
			return result;
		}
		else
			throw new TypeException("Value " + value + " cannot be used in this context.");
	}
	
	@Override
	public Boolean getValue()
	{
		return bool;
	}

}
