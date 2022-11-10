package util;

import util.expressions.Expression;
import util.tokens.Token;
import util.values.Value;

/**
 * Base class for tokens representing operators or similar.
 * @author UFFR
 *
 */
public interface Operation extends Token
{
	/**
	 * Construct an expression given this operation type and the two parameters.
	 * @param left The value on the left or the main value if a function.
	 * @param right The value on the right or the base if a function. May be null in the latter case.
	 * @return The {@link Expression} object made as a result.
	 */
	public Expression constructExpression(Value<?> left, Value<?> right);
	/**
	 * If the expression takes only a single value.
	 * @return True if it takes only 1 value, false if it needs 2.
	 */
	public boolean singleArg();
}
