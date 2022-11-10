package util.expressions;

import java.util.List;
import java.util.Objects;

import exceptions.TypeException;
import main.Evaluator;
import main.Main;
import util.tokens.Token;
import util.values.Value;

/**
 * A generic nested expression. Must go back to a {@link Parser} to be evaluated, usually recursively.
 * @author UFFR
 *
 */
public class NestedExpression implements Expression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4870016874019111425L;
	protected final List<Token> expression;
	
	public NestedExpression(List<Token> list)
	{
		this.expression = list;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(expression);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof NestedExpression))
			return false;
		final NestedExpression other = (NestedExpression) obj;
		return Objects.equals(expression, other.expression);
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder().append('(');
		expression.forEach(builder::append);
		return builder.append(')').toString();
	}

	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		return Evaluator.evaluateParsedExpression(Main.PARSER.apply(expression));
	}

}
