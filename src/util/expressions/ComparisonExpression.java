package util.expressions;

import java.util.Objects;

import exceptions.TypeException;
import util.CompareType;
import util.values.BooleanValue;
import util.values.Value;

/**
 * Comparison operations.
 * @author UFFR
 *
 */
public class ComparisonExpression implements Expression
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1051324555519691948L;
	private final Value<?> left, right;
	private final CompareType compareType;
	
	public ComparisonExpression(Value<?> left, Value<?> right, CompareType compareType)
	{
		this.left = left;
		this.right = right;
		this.compareType = compareType;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(compareType, left, right);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof ComparisonExpression))
			return false;
		final ComparisonExpression other = (ComparisonExpression) obj;
		return compareType == other.compareType && Objects.equals(left, other.left)
				&& Objects.equals(right, other.right);
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append(left).append(' ').append(compareType).append(' ').append(right).toString();
	}
	
	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		return new BooleanValue(left.compare(right, compareType, false));
	}

}
