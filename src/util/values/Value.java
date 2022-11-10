package util.values;

import java.io.Serializable;

import exceptions.TypeException;
import util.CompareType;
import util.tokens.Token;

/**
 * The interface type that allows certain objects have mathematical operations evaluated on an underlying object.
 * @author UFFR
 *
 * @param <T> The type of the underlying object.
 */
public interface Value<T> extends Serializable, Token
{
	@Override
	public int hashCode();
	@Override
	public boolean equals(Object obj);
	@Override
	public String toString();
	
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException;
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException;
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException;
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException;
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException;
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException;
	public Value<?> abs() throws UnsupportedOperationException, TypeException;
	public Value<?> negate() throws UnsupportedOperationException, TypeException;
	public Value<?> round() throws UnsupportedOperationException, TypeException;
	public Value<?> factorial() throws UnsupportedOperationException, TypeException;
	public boolean compare(Value<?> value, CompareType type, boolean printOut) throws UnsupportedOperationException, TypeException;
	public T getValue();
}
