package util.values;

import java.util.Objects;

import com.google.common.annotations.Beta;

import exceptions.TypeException;
import util.CompareType;
import util.HybridNumber;

/**
 * Value wrapper for the experimental numeric class {@link HybridNumber}.<br>
 * Subject to change.
 * @author UFFR
 *
 */
@Beta
public class HybridNumberValue extends Number implements Value<HybridNumber>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -763790663537172308L;
	private final HybridNumber number;
	public HybridNumberValue(HybridNumber number)
	{
		this.number = number;
	}

	@Override
	public TokenType getType()
	{
		return TokenType.VALUE;
	}

	@Override
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		return null;
	}

	@Override
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> round() throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compare(Value<?> value, CompareType type, boolean printOut)
			throws UnsupportedOperationException, TypeException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HybridNumber getValue()
	{
		return number;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(number);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof HybridNumberValue))
			return false;
		final HybridNumberValue other = (HybridNumberValue) obj;
		return Objects.equals(number, other.number);
	}

	@Override
	public String toString()
	{
		return number.toString();
	}
	
	@Override
	public double doubleValue()
	{
		return number.doubleValue();
	}

	@Override
	public float floatValue()
	{
		return number.floatValue();
	}

	@Override
	public int intValue()
	{
		return number.intValue();
	}

	@Override
	public long longValue()
	{
		return number.longValue();
	}
	
}
