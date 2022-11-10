package util.expressions;

import java.math.BigDecimal;
import java.util.Objects;

import exceptions.TypeException;
import main.Evaluator;
import util.Function;
import util.values.Value;

/**
 * An expression containing an intermediary step to evaluate a function.
 * @author UFFR
 *
 */
public class FunctionExpression implements Expression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4668258256040767317L;
	private final Value<?> input;
	private final BigDecimal base;
	private final Function function;
	public FunctionExpression(Value<?> input, BigDecimal base, Function function)
	{
		this.input = input;
		this.base = base;
		this.function = function;
	}
	
	public FunctionExpression(Value<?> input, Function function)
	{
		this(input, null, function);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof FunctionExpression))
			return false;
		final FunctionExpression other = (FunctionExpression) obj;
		return Objects.equals(base, other.base) && function == other.function && Objects.equals(input, other.input);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(base, function, input);
	}
	
	@Override
	public String toString()
	{
		if (function == Function.ABS)
			return '|' + input.toString() + '|';
		else
		{
			final StringBuilder builder = new StringBuilder(function.toString().toLowerCase()).append('(');
			if (base != null)
				builder.append(base).append(", ");
			return builder.append(input).append(')').toString();
		}
	}
	
	@Override
	public Value<?> getValue()
	{
		return evaluate();
	}

	@Override
	public Value<?> evaluate() throws UnsupportedOperationException, TypeException
	{
		return Evaluator.evaluateFunctionGeneric(function, base, input);
	}

}
