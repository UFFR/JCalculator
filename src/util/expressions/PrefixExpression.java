package util.expressions;

import java.util.Objects;

import exceptions.TypeException;
import util.OperationType;
import util.values.Value;

/**
 * Prefix operations, usually just '-' for negation.
 * @author UFFR
 *
 */
public class PrefixExpression implements Expression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6502105797566359447L;
	private final Expression operand;
	private final OperationType type;
	
	public PrefixExpression(Expression operand, OperationType type)
	{
		this.operand = operand;
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(operand, type);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof PrefixExpression))
			return false;
		final PrefixExpression other = (PrefixExpression) obj;
		return Objects.equals(operand, other.operand) && type == other.type;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("PrefixExpression [operand=").append(operand).append(", type=").append(type).append(']');
		return builder.toString();
	}

	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		if (type == OperationType.SUBTRACTION)
			return operand.negate();
		else
			throw new UnsupportedOperationException("Operator [" + type + "] cannot be used as a prefix!");
	}

}
