package main;

import static main.Main.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import ch.obermuhlner.math.big.DefaultBigDecimalMath;
import exceptions.SyntaxException;
import exceptions.TypeException;
import util.Function;
import util.Operation;
import util.expressions.Expression;
import util.values.*;

/**
 * Primary class to evaluate parsed input and functions.
 * @author UFFR
 *
 */
public class Evaluator
{
	/**
	 * Evaluate a {@link Parser} object.
	 * @param parser The {@code Parser} to evaluate.
	 * @return The final value after all operations are complete.
	 * @throws TypeException If an operation between two values failed because one didn't support the other type.
	 * @throws UnsupportedOperationException If a value didn't support a certain operation.
	 * @throws SyntaxException If a malformed token or value slipped past the {@code Parser} and caused an error.
	 */
	public static Value<?> evaluateParsedExpression(Parser parser) throws TypeException, UnsupportedOperationException, SyntaxException
	{
		return evaluateParsedExpression(parser.getValues(), parser.getOperators());
	}
	
	/**
	 * Evaluate two {@link Deque} objects, presumably created by a {@link Parser}. Goes in order of left-to-right, not by precedence, order has to be done by the parser first.
	 * @param values The value {@code Deque}, behaves as a {@code stack}.
	 * @param operators The operator {@code Deque}, behaves as a {@code stack}.
	 * @return The final value after all operations are complete.
	 * @throws TypeException If an operation between two values failed because one didn't support the other type.
	 * @throws UnsupportedOperationException If a value didn't support a certain operation.
	 * @throws SyntaxException If a malformed token or value slipped past the {@code Parser} and caused an error.
	 */
	public static Value<?> evaluateParsedExpression(Deque<Value<?>> values, Deque<Operation> operators) throws TypeException, UnsupportedOperationException, SyntaxException
	{
		while (!operators.isEmpty())
		{
			if (operators.peek().singleArg())
				values.offer(operators.poll().constructExpression(values.poll(), null).evaluate());
			else
				values.offer(operators.poll().constructExpression(values.poll(), values.poll()).evaluate());
		}
		
		if (values.peek() instanceof Expression)
			return ((Expression) values.poll()).evaluate();
		
		return values.poll();
	}
	
	/**
	 * Evaluate a {@link Function} with up to two arguments, noted below. May call {@link #evaluateFunctionList(Function, BigDecimal, ListValue)} and/or {@link #evaluateFunctionMatrix(Function, BigDecimal, MatrixValue)} if those types are found.
	 * @param function The {@code Function} type to evaluate.
	 * @param base The base of the function, if applicable, may be {@code null}.
	 * @param input The main input of the function.
	 * @return The final value after the operation is complete.
	 */
	public static Value<?> evaluateFunctionGeneric(Function function, BigDecimal base, Value<?> input)
	{
		if (input instanceof ListValue)
			return evaluateFunctionList(function, base, (ListValue) input);
		if (input instanceof MatrixValue)
			return evaluateFunctionMatrix(function, base, (MatrixValue) input);
		if (!(input instanceof NumberValue))
			throw new TypeException("Value " + input + " is not applicable in this context for the " + function + " function.");
		if (function.singleArg && base != null)
			throw new SyntaxException("Function " + function + " does not take any additional parameters.");
		final BigDecimal numInput = (BigDecimal) input.getValue(), result;
		switch (function)
		{
			case ABS: result = numInput.abs(); break;
			
			case ROOT: result = DefaultBigDecimalMath.root(numInput, base == null ? BigDecimal.valueOf(2) : base); break;
			case LN: result = DefaultBigDecimalMath.log(numInput); break;
			case LOG: result = DefaultBigDecimalMath.log10(numInput); break;
			
			case SIN: result = DefaultBigDecimalMath.sin(numInput); break;
			case COS: result = DefaultBigDecimalMath.cos(numInput); break;
			case TAN: result = DefaultBigDecimalMath.tan(numInput); break;
			
			case ARCSIN: result = DefaultBigDecimalMath.asin(numInput); break;
			case ARCCOS: result = DefaultBigDecimalMath.acos(numInput); break;
			case ARCTAN: result = (base == null ? DefaultBigDecimalMath.atan(numInput)
								: DefaultBigDecimalMath.atan2(numInput, base)); break;
			case CSC: result = BigDecimal.ONE.divide(DefaultBigDecimalMath.sin(numInput), getMathContext()); break;
			case SEC: result = BigDecimal.ONE.divide(DefaultBigDecimalMath.cos(numInput), getMathContext()); break;
			case COT: result = DefaultBigDecimalMath.cot(numInput); break;
			
			case ARCCSC: result = DefaultBigDecimalMath.asin(BigDecimal.ONE.divide(numInput, getMathContext())); break;
			case ARCSEC: result = DefaultBigDecimalMath.acos(BigDecimal.ONE.divide(numInput, getMathContext())); break;
			case ARCCOT: result = DefaultBigDecimalMath.acot(numInput); break;
			
			case CEIL: result = numInput.round(new MathContext(getPrecision(), RoundingMode.CEILING)); break;
			case FLOOR: result = numInput.round(new MathContext(getPrecision(), RoundingMode.FLOOR)); break;
			case ROUND: result = (BigDecimal) input.round(); break;
			default: throw new IllegalStateException("Could not interpret function type: " + function + '!');
		}
		return new NumberValue(result);
	}
	
	/**
	 * {@link #evaluateFunctionGeneric(Function, BigDecimal, Value)}, but specifically for {@link ListValue}. Used recursively in the aforementioned method.
	 * @param function The {@link Function} type to evaluate.
	 * @param base The base of the function, if applicable, may be {@code null}.
	 * @param listValue The list to evaluate the function on.
	 * @return A new list with each internal value evaluated with the function.
	 */
	public static Value<?> evaluateFunctionList(Function function, BigDecimal base, ListValue listValue)
	{
		final List<Value<?>> newValues = new ArrayList<Value<?>>(listValue.size());
		for (Value<?> value : listValue)
			newValues.add(evaluateFunctionGeneric(function, base, value));
		return new ListValue(newValues);
	}
	
	/**
	 * {@link #evaluateFunctionGeneric(Function, BigDecimal, Value)}, but specifically for {@link MatrixValue}. Used recursively in the aforementioned method.
	 * @param function The {@link Function} type to evaluate.
	 * @param base The base of the function, if applicable, may be {@code null}.
	 * @param matrixValue The matrix to evaluate the function on.
	 * @return A new matrix with each internal value evaluated with the function.
	 */
	public static Value<?> evaluateFunctionMatrix(Function function, BigDecimal base, MatrixValue matrixValue)
	{
		final Value<?>[][] otherMatrix = matrixValue.getValue(), newMatrix = new Value[matrixValue.getRows()][matrixValue.getColumns()];
		for (int row = 0; row < matrixValue.getRows(); row++)
			for (int column = 0; column < matrixValue.getColumns(); column++)
				newMatrix[row][column] = evaluateFunctionGeneric(function, base, otherMatrix[row][column]);
		return new MatrixValue(newMatrix);
	}
}
