package util.expressions;

import exceptions.TypeException;
import util.CompareType;
import util.values.Value;

/**
 * An intermediary value, does not directly hold a value, must be evaluated first.
 * @author UFFR
 *
 */
public interface Expression extends Value<Object>
{
	@Override
	default TokenType getType()
	{
		return TokenType.EXPRESSION;
	}
	
	@Override
	public boolean equals(Object obj);
	@Override
	public int hashCode();
	@Override
	public String toString();
	
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException;
	
	@Override
	default Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		return evaluate().abs();
	}
	
	@Override
	default Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		return evaluate().addition(augend);
	}
	
	@Override
	default boolean compare(Value<?> value, CompareType type, boolean printOut)
			throws UnsupportedOperationException, TypeException
	{
		return evaluate().compare(value, type, printOut);
	}
	
	@Override
	default Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		return evaluate().division(divisor);
	}

	@Override
	default Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		return evaluate().exponentiate(exponent);
	}
	
	@Override
	default Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		return evaluate().modulo(divisor);
	}
	
	@Override
	default Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		return evaluate().multiplication(multiplicand);
	}
	
	@Override
	default Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		return evaluate().negate();
	}
	
	@Override
	default Value<?> round() throws UnsupportedOperationException, TypeException
	{
		return evaluate().round();
	}
	
	@Override
	default Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		return evaluate().factorial();
	}
	
	@Override
	default Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		return evaluate().subtraction(subtrahend);
	}
	
	@Override
	default Value<?> getValue()
	{
		return evaluate();
	}
}
