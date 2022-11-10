package util.expressions;

import java.util.List;

import exceptions.TypeException;
import util.tokens.Token;
import util.values.Value;

/**
 * A specification of a {@link NestedExpression} for evaluating absolute values.
 * @author UFFR
 *
 */
public class AbsoluteValueExpression extends NestedExpression
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4596680561563979889L;

	public AbsoluteValueExpression(List<Token> expression)
	{
		super(expression);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AbsoluteValueExpression))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder().append('|');
		expression.forEach(builder::append);
		return builder.append('|').toString();
	}

	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		return super.evaluate().abs();
	}

}
