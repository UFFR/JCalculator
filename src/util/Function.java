package util;

import exceptions.SyntaxException;
import util.expressions.Expression;
import util.expressions.FunctionExpression;
import util.values.NumberValue;
import util.values.Value;

/**
 * Enumeration for mathematical and utility functions.
 * @author UFFR
 *
 */
public enum Function implements Operation
{
	ABS(true),
	
	ROOT(false),
	LN(true),
	LOG(true),
	
	SIN(true),
	COS(true),
	TAN(true),
	
	ARCSIN(true),
	ARCCOS(true),
	ARCTAN(false),
	
	CSC(true),
	SEC(true),
	COT(true),
	
	ARCCSC(true),
	ARCSEC(true),
	ARCCOT(true),
	
	CEIL(true),
	FLOOR(true),
	ROUND(true);
	public final boolean singleArg;
	private Function(boolean singleArg)
	{
		this.singleArg = singleArg;
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.TEXT;
	}
	
	@Override
	public Expression constructExpression(Value<?> left, Value<?> right)
	{
		// Functions typically only take one value and if there is a second, it is always a NumberValue.
		try
		{
			return new FunctionExpression(left, right == null ? null : ((NumberValue) right).getValue(), this);
		} catch (ClassCastException e)
		{
			throw new SyntaxException("Second value isn't a NumberValue!", e);
		}
	}
	
	@Override
	public boolean singleArg()
	{
		return singleArg;
	}
	
	public static Function functionFromString(String name) throws SyntaxException
	{
		try
		{
			return valueOf(name);
		} catch (Exception e)
		{
			throw new SyntaxException("Unknown function type: " + name, e);
		}
	}
}
