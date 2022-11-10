package util;

import util.expressions.ComparisonExpression;
import util.expressions.Expression;
import util.values.Value;

/**
 * Enumeration for comparators.
 * @author UFFR
 *
 */
public enum CompareType implements Operation
{
	EQUALS("="),
	NOT_EQUALS("!="),
	LESS_THAN("<"),
	GREATER_THAN(">"),
	LESS_THAN_OR_EQUAL("<="),
	GREATER_THAN_OR_EQUAL(">=");
	
	public final String symbol;
	
	private CompareType(String symbol)
	{
		this.symbol = symbol;
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.COMPARATOR;
	}
	
	@Override
	public Expression constructExpression(Value<?> left, Value<?> right)
	{
		return new ComparisonExpression(left, right, this);
	}
	
	@Override
	public boolean singleArg()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return symbol;
	}
	
	public static CompareType getCompareType(String symbol)
	{
		switch (symbol)
		{
			case "=": return EQUALS;
			case "!=": return NOT_EQUALS;
			case "<": return LESS_THAN;
			case ">": return GREATER_THAN;
			case "<=": return LESS_THAN_OR_EQUAL;
			case ">=": return GREATER_THAN_OR_EQUAL;
			default: return null;
		}
	}
}
