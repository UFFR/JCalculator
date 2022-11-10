package util;

import exceptions.SyntaxException;
import util.expressions.ArithmeticExpression;
import util.expressions.Expression;
import util.expressions.FactorialExpression;
import util.values.Value;

/**
 * Enumeration for the basic arithmetic operations. Has a single character symbol ({@link #getSymbol()}) and the precedence ({@link #getPrecedence()}).
 * @author UFFR
 *
 */
public enum OperationType implements Operation
{
	ADDITION('+', 1),
	SUBTRACTION('-', 0),
	MULTIPLICATION('*', 3),
	DIVISION('/', 2),
	EXPONENTIATION('^', 4),
	MODULUS('%', 2),
	FACTORIAL('!', 5);
	private final char symbol;
	private final byte precedence;
	private OperationType(char symbol, int precedence)
	{
		this.symbol = symbol;
		this.precedence = (byte) precedence;
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.OPERATOR;
	}
	
	@Override
	public Expression constructExpression(Value<?> left, Value<?> right)
	{
		return this == FACTORIAL ? new FactorialExpression(left) : new ArithmeticExpression(left, right, this);
	}
	
	@Override
	public boolean singleArg()
	{
		return this == FACTORIAL;
	}
	
	/**
	 * Get the symbol that represents this arithmetic operation.
	 * @return The {@code char} that this operation is represented by.
	 */
	public char getSymbol()
	{
		return symbol;
	}
	
	/**
	 * Get the precedence of this operation. Larger numbers mean higher precedence.<br>
	 * See {@link #rightPrecedence(OperationType, OperationType)} to compare two operators on whether precedence takes or left-to-right order should take place.
	 * @return A {@code byte} representing the precedence over other operators.
	 */
	public byte getPrecedence()
	{
		return precedence;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(symbol);
	}
	
	/**
	 * Get the operation given the symbol that represents it.
	 * @param symbol The character symbol.
	 * @return The operation that the symbol represents.
	 * @throws SyntaxException If the symbol does not represent a valid operator.
	 */
	public static OperationType getOperation(char symbol) throws SyntaxException
	{
		switch (symbol)
		{
			case '+': return ADDITION;
			case '-': return SUBTRACTION;
			case '*': return MULTIPLICATION;
			case '/': return DIVISION;
			case '^': return EXPONENTIATION;
			case '!': return FACTORIAL;
			case '%': return MODULUS;
			default: throw new SyntaxException("Unrecognized operator token: " + symbol);
		}
	}
	
	/**
	 * Test whether or not precedence has higher importance than left-to-right operation.
	 * @param left The operator to the left.
	 * @param right The operator to the right.
	 * @return True, if the right operator should be evaluated first, false if the left one should be.
	 */
	public static boolean rightPrecedence(OperationType left, OperationType right)
	{
		return left.getPrecedence() < right.getPrecedence();
	}
	
}
