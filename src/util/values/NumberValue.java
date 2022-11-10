package util.values;

import static main.Main.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.DefaultBigDecimalMath;
import exceptions.TypeException;
import util.CompareType;

/**
 * The base {@code Value} type, supports all operations. Wraps a {@link BigDecimal} object.
 * @author UFFR
 *
 */
public class NumberValue extends Number implements Value<BigDecimal>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4838987114451035592L;
	private final BigDecimal number;
	
	public NumberValue(BigDecimal number)
	{
		this.number = number.stripTrailingZeros();
	}
	
	public NumberValue(String number)
	{
		this(BigDecimalMath.toBigDecimal(number));
	}

	@Override
	public TokenType getType()
	{
		return TokenType.VALUE;
	}
	
	@Override
	public int hashCode()
	{
		return number.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		final BigDecimal toTest;
		if (obj instanceof NumberValue)
			toTest = ((NumberValue) obj).number;
		else if (obj instanceof BigDecimal)
			toTest = (BigDecimal) obj;
		else if (obj instanceof BigInteger)
			toTest = new BigDecimal((BigInteger) obj);
		else
			return false;
		return number.equals(toTest);
	}

	@Override
	public String toString()
	{
		return number.toPlainString();
	}

	@Override
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		if (augend instanceof NumberValue)
			return new NumberValue(number.add(((NumberValue) augend).number, getMathContext()).stripTrailingZeros());
		if (augend instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) augend).getValue();
			final ArrayList<Value<?>> newValues = new ArrayList<>(otherValues.size());
			for (Value<?> v : otherValues)
				newValues.add(v.addition(this));
			return new ListValue(newValues);
		}
		throw new TypeException("Value [" + augend.getClass().getSimpleName() + "] cannot be used in this context.");
	}

	@Override
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		if (subtrahend instanceof NumberValue)
			return new NumberValue(number.subtract(((NumberValue) subtrahend).number, getMathContext()).stripTrailingZeros());
		if (subtrahend instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) subtrahend).getValue();
			final ArrayList<Value<?>> newValues = new ArrayList<>(otherValues.size());
			for (Value<?> v : otherValues)
				newValues.add(v.subtraction(this));
			return new ListValue(newValues);
		}
		throw new TypeException("Value [" + subtrahend.getClass().getSimpleName() + "] cannot be used in this context.");
	}

	@Override
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		if (multiplicand instanceof NumberValue)
			return new NumberValue(number.multiply(((NumberValue) multiplicand).number, getMathContext()).stripTrailingZeros());
		if (multiplicand instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) multiplicand).getValue();
			final ArrayList<Value<?>> newValues = new ArrayList<>(otherValues.size());
			for (Value<?> v : otherValues)
				newValues.add(v.multiplication(this));
			return new ListValue(newValues);
		}
		if (multiplicand instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) multiplicand;
			final Value<?>[][] otherMatrix = other.getValue();
			final Value<?>[][] newMatrix = new Value[other.getRows()][other.getColumns()];
			for (int row = 0; row < other.getRows(); row++)
				for (int column = 0; column < other.getColumns(); column++)
					newMatrix[row][column] = otherMatrix[row][column].multiplication(this);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value [" + multiplicand.getClass().getSimpleName() + "] cannot be used in this context.");
	}

	@Override
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		if (divisor instanceof NumberValue)
			return new NumberValue(number.divide(((NumberValue) divisor).number, getMathContext()).stripTrailingZeros());
		if (divisor instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) divisor).getValue();
			final ArrayList<Value<?>> newValues = new ArrayList<>(otherValues.size());
			for (Value<?> v : otherValues)
				newValues.add(v.division(this));
			return new ListValue(newValues);
		}
		if (divisor instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) divisor;
			final Value<?>[][] otherMatrix = other.getValue();
			final Value<?>[][] newMatrix = new Value[other.getRows()][other.getColumns()];
			for (int row = 0; row < other.getRows(); row++)
				for (int column = 0; column < other.getColumns(); column++)
					newMatrix[row][column] = otherMatrix[row][column].division(this);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value type [" + divisor.getClass().getSimpleName() + "] cannot be used in this context.");
	}

	@Override
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		return new NumberValue(number.pow(exponent, getMathContext()).stripTrailingZeros());
	}

	@Override
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		if (divisor instanceof NumberValue)
			return new NumberValue(number.remainder(((NumberValue) divisor).number, getMathContext()).stripTrailingZeros());
		throw new TypeException("Value type [" + divisor.getClass().getSimpleName() + "] cannot be used in this context.");
	}

	@Override
	public Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		return new NumberValue(number.abs().stripTrailingZeros());
	}

	@Override
	public Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		return new NumberValue(number.negate().stripTrailingZeros());
	}

	@Override
	public Value<?> round() throws UnsupportedOperationException, TypeException
	{
		return new NumberValue(DefaultBigDecimalMath.round(number).stripTrailingZeros());
	}
	
	@Override
	public Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		return new NumberValue(DefaultBigDecimalMath.factorial(number).stripTrailingZeros());
	}
	
	@Override
	public boolean compare(Value<?> value, CompareType type, boolean printOut)
			throws UnsupportedOperationException, TypeException
	{
		final boolean result;
		final BigDecimal toCompare;
		if (value instanceof NumberValue)
			toCompare = ((NumberValue) value).number;
		else
			throw new IllegalArgumentException("Value type [" + value.getClass().getSimpleName() + "] cannot be used for this operation within this context.");
		switch (type)
		{
			case EQUALS: result = number.compareTo(toCompare) == 0; break;
			case GREATER_THAN: result = number.compareTo(toCompare) > 0; break;
			case GREATER_THAN_OR_EQUAL: result = number.compareTo(toCompare) >= 0; break;
			case LESS_THAN: result = number.compareTo(toCompare) < 0; break;
			case LESS_THAN_OR_EQUAL: result = number.compareTo(toCompare) <= 0; break;
			case NOT_EQUALS: result = number.compareTo(toCompare) != 0; break;
			default: result = false; break;
		}
		if (printOut)
			getPrinter().println(result);
		return result;
	}

	@Override
	public BigDecimal getValue()
	{
		return number;
	}
	
	@Override
	public double doubleValue()
	{
		return number.doubleValue();
	}

	@Override
	public float floatValue()
	{
		return number.floatValue();
	}

	@Override
	public int intValue()
	{
		return number.intValueExact();
	}

	@Override
	public long longValue()
	{
		return number.longValueExact();
	}

}
