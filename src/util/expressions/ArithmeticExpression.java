package util.expressions;

import java.util.Objects;

import exceptions.SyntaxException;
import exceptions.TypeException;
import util.OperationType;
import util.values.NumberValue;
import util.values.Value;

/**
 * The standard arithmetic operation.
 * @author UFFR
 *
 */
public class ArithmeticExpression implements Expression
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2927109086644929849L;
	private final Value<?> left, right;
	private final OperationType operationType;
	public ArithmeticExpression(Value<?> left, Value<?> right, OperationType operationType)
	{
		this.left = left;
		this.right = right;
		this.operationType = Objects.requireNonNull(operationType, "Null operator detected!");
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof ArithmeticExpression))
			return false;
		final ArithmeticExpression other = (ArithmeticExpression) obj;
		return (Objects.equals(left, other.left) || Objects.equals(right, other.left))
				&& operationType == other.operationType
				&& (Objects.equals(right, other.right) || Objects.equals(left, other.right));
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(left, operationType, right);
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder().append(left).append(' ').append(operationType).append(' ').append(right).toString();
	}
	
	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		switch (operationType)
		{
			case ADDITION: return left.addition(right);
			case DIVISION: return left.division(right);
			case EXPONENTIATION:
				if (right instanceof NumberValue)
				{
					try
					{
						return left.exponentiate(((NumberValue) right).intValue());
					} catch (ArithmeticException e)
					{
						throw new SyntaxException("Exponent could not be parsed as a 32-bit integer.", e);
					}
				} else
					throw new TypeException("Value " + right + " cannot be used in this context.");
			case MODULUS: return left.modulo(right);
			case MULTIPLICATION: return left.multiplication(right);
			case SUBTRACTION: return left.subtraction(right);
			default: return null;
		}
	}

	@Override
	public Value<?> getValue()
	{
		return evaluate();
	}
}
