package util.values;

import java.util.Arrays;

import exceptions.DimensionException;
import exceptions.TypeException;
import main.Main;
import util.CompareType;

/**
 * A wrapper around a 2D array of {@code Value} objects.<br>
 * Operations apply to each internal value, other matrices have special behavior as per mathematics.
 * @author UFFR
 *
 */
public class MatrixValue implements Value<Value<?>[][]>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2808829310746285493L;
	private final int rows, columns;
	private final Value<?>[][] matrix;
	
	public MatrixValue(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		matrix = new Value[rows][columns];
	}
	
	public MatrixValue(Value<?>[][] matrix)
	{
		this.matrix = matrix;
		rows = matrix.length;
		columns = matrix[0].length;
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.VALUE;
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.deepHashCode(matrix);
	}

	@Override
	public boolean equals(Object obj)
	{
		final Value<?>[][] toTest;
		if (obj instanceof MatrixValue)
			toTest = ((MatrixValue) obj).matrix;
		else if (obj instanceof Value<?>[][])
			toTest = (Value<?>[][]) obj;
		else
			return false;
		return Arrays.deepEquals(matrix, toTest);
	}

	@Override
	public String toString()
	{
		return Arrays.deepToString(matrix);
	}

	@Override
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		if (augend instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) augend;
			if (rows == other.rows && columns == other.columns)
			{
				final Value<?>[][] otherMatrix = other.getValue();
				final Value<?>[][] newMatrix = new Value[rows][columns];
				for (int row = 0; row < rows; row++)
					for (int column = 0; column < columns; column++)
						newMatrix[row][column] = matrix[row][column].addition(otherMatrix[row][column]);
				return new MatrixValue(newMatrix);
			} else
				throw new DimensionException("Dimension mismatch.");
		}
		if (augend instanceof NumberValue)
		{
			final Value<?>[][] newMatrix = new Value[rows][columns];
			for (int row = 0; row < rows; row++)
				for (int column = 0; column < columns; column++)
					newMatrix[row][column] = matrix[row][column].addition(augend);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value " + augend + " cannot be used in this context.");
	}

	@Override
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		if (subtrahend instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) subtrahend;
			if (rows == other.rows && columns == other.columns)
			{
				final Value<?>[][] otherMatrix = other.getValue();
				final Value<?>[][] newMatrix = new Value[rows][columns];
				for (int row = 0; row < rows; row++)
					for (int column = 0; column < columns; column++)
						newMatrix[row][column] = matrix[row][column].subtraction(otherMatrix[row][column]);
				return new MatrixValue(newMatrix);
			} else
				throw new DimensionException("Dimension mismatch.");
		}
		if (subtrahend instanceof NumberValue)
		{
			final Value<?>[][] newMatrix = new Value[rows][columns];
			for (int row = 0; row < rows; row++)
				for (int column = 0; column < columns; column++)
					newMatrix[row][column] = matrix[row][column].subtraction(subtrahend);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value " + subtrahend + " cannot be used in this context.");
	}

	@Override
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		if (multiplicand instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) multiplicand;
			if (rows == other.rows && columns == other.columns)
			{
				final Value<?>[][] otherMatrix = other.getValue();
				final Value<?>[][] newMatrix = new Value[rows][columns];
				for (int row = 0; row < rows; row++)
					for (int column = 0; column < columns; column++)
						newMatrix[row][column] = matrix[row][column].multiplication(otherMatrix[row][column]);
				return new MatrixValue(newMatrix);
			} else
				throw new DimensionException("Dimension mismatch.");
		}
		if (multiplicand instanceof NumberValue)
		{
			final Value<?>[][] newMatrix = new Value[rows][columns];
			for (int row = 0; row < rows; row++)
				for (int column = 0; column < columns; column++)
					newMatrix[row][column] = matrix[row][column].multiplication(multiplicand);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value " + multiplicand + " cannot be used in this context.");
	}

	@Override
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		if (divisor instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) divisor;
			if (rows == other.rows && columns == other.columns)
			{
				final Value<?>[][] otherMatrix = other.getValue();
				final Value<?>[][] newMatrix = new Value[rows][columns];
				for (int row = 0; row < rows; row++)
					for (int column = 0; column < columns; column++)
						newMatrix[row][column] = matrix[row][column].division(otherMatrix[row][column]);
				return new MatrixValue(newMatrix);
			} else
				throw new DimensionException("Dimension mismatch.");
		}
		if (divisor instanceof NumberValue)
		{
			final Value<?>[][] newMatrix = new Value[rows][columns];
			for (int row = 0; row < rows; row++)
				for (int column = 0; column < columns; column++)
					newMatrix[row][column] = matrix[row][column].division(divisor);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value " + divisor + " cannot be used in this context.");
	}

	@Override
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		final Value<?>[][] newMatrix = new Value[rows][columns];
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				newMatrix[row][column] = matrix[row][column].exponentiate(exponent);
		return new MatrixValue(newMatrix);
	}

	@Override
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		if (divisor instanceof MatrixValue)
		{
			final MatrixValue other = (MatrixValue) divisor;
			if (rows == other.rows && columns == other.columns)
			{
				final Value<?>[][] otherMatrix = other.getValue();
				final Value<?>[][] newMatrix = new Value[rows][columns];
				for (int row = 0; row < rows; row++)
					for (int column = 0; column < columns; column++)
						newMatrix[row][column] = matrix[row][column].division(otherMatrix[row][column]);
				return new MatrixValue(newMatrix);
			} else
				throw new DimensionException("Dimension mismatch.");
		}
		if (divisor instanceof NumberValue)
		{
			final Value<?>[][] newMatrix = new Value[rows][columns];
			for (int row = 0; row < rows; row++)
				for (int column = 0; column < columns; column++)
					newMatrix[row][column] = matrix[row][column].division(divisor);
			return new MatrixValue(newMatrix);
		}
		throw new TypeException("Value " + divisor + " cannot be used in this context.");
	}

	@Override
	public Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		final Value<?>[][] newMatrix = new Value[rows][columns];
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				newMatrix[row][column] = matrix[row][column].abs();
		return new MatrixValue(newMatrix);
	}

	@Override
	public Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		final Value<?>[][] newMatrix = new Value[rows][columns];
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				newMatrix[row][column] = matrix[row][column].negate();
		return new MatrixValue(newMatrix);
	}

	@Override
	public Value<?> round() throws UnsupportedOperationException, TypeException
	{
		final Value<?>[][] newMatrix = new Value[rows][columns];
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				newMatrix[row][column] = matrix[row][column].round();
		return new MatrixValue(newMatrix);
	}
	
	@Override
	public Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		final Value<?>[][] newMatrix = new Value[rows][columns];
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				newMatrix[row][column] = matrix[row][column].factorial();
		return null;
	}

	@Override
	public boolean compare(Value<?> value, CompareType type, boolean printOut)
			throws UnsupportedOperationException, TypeException
	{
		if (!(value instanceof MatrixValue))
			throw new IllegalArgumentException("Value " + value + " cannot be used for this operation within this context.");
		final MatrixValue other = (MatrixValue) value;
		final Value<?>[][] toCompare = other.matrix;
		if (rows != other.rows && columns != other.columns)
			return false;
		final boolean[][] comparisons = new boolean[rows][columns];
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				comparisons[row][column] = matrix[row][column].compare(toCompare[row][column], type, false);
		if (printOut)
			Main.getPrinter().println(Arrays.deepToString(comparisons));
		for (int row = 0; row < comparisons.length; row++)
			for (int column = 0; column < comparisons[0].length; column++)
				if (!comparisons[row][column])
					return false;
		return true;
	}
	
	@Override
	public Value<?>[][] getValue()
	{
		return matrix;
	}
	
	public Value<?> getValue(int row, int column)
	{
		return matrix[row][column];
	}
	
	public int getRows()
	{
		return rows;
	}
	
	public int getColumns()
	{
		return columns;
	}
}
