package util.expressions;

import java.math.BigDecimal;
import java.util.Objects;

import ch.obermuhlner.math.big.DefaultBigDecimalMath;
import exceptions.TypeException;
import util.values.NumberValue;
import util.values.Value;

/**
 * A factorial ('!') expression, for the only suffix operator.
 * @author UFFR
 *
 */
public class FactorialExpression implements Expression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6656889856524392780L;
	private final Value<?> value;
	
	public FactorialExpression(Value<?> value)
	{
		this.value = value;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof FactorialExpression))
			return false;
		final FactorialExpression other = (FactorialExpression) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public String toString()
	{
		return evaluate().toString();
	}
	
	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		return new NumberValue(DefaultBigDecimalMath.factorial((BigDecimal) value.getValue()));
	}

}
