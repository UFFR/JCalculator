package util.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ImmutableList;

import exceptions.DimensionException;
import exceptions.TypeException;
import main.Main;
import util.CompareType;

/**
 * A wrapper for a {@code List} that contains {@code Value} objects.<br>
 * Operations apply to each internal value. May get recursive if the internal values are also lists or matrices ({@link MatrixValue}).
 * @author UFFR
 *
 */
public class ListValue implements Value<List<Value<?>>>, List<Value<?>>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2306360219703750975L;
	private final List<Value<?>> values;
	
	public ListValue(Collection<Value<?>> values)
	{
		this.values = ImmutableList.copyOf(values);
	}
	
	@Override
	public TokenType getType()
	{
		return TokenType.VALUE;
	}
	
	@Override
	public int hashCode()
	{
		return values.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		final List<Value<?>> toTest;
		if (obj instanceof ListValue)
			toTest = ((ListValue) obj).values;
		else if (obj instanceof List<?>)
			toTest = (List<Value<?>>) obj;
		else
			return false;
		return values.equals(toTest);
	}

	@Override
	public String toString()
	{
		return '{' + values.toString().substring(1, values.toString().length() - 1) + '}';
	}

	@Override
	public Value<?> addition(Value<?> augend) throws UnsupportedOperationException, TypeException
	{
		if (augend instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) augend).getValue();
			if (otherValues.size() != values.size())
				throw new DimensionException("Dimension mismatch.");
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (int i = 0; i < values.size(); i++)
				newValues.add(values.get(i).addition(otherValues.get(i)));
			return new ListValue(newValues);
		}
		if (augend instanceof NumberValue)
		{
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (Value<?> value : values)
				newValues.add(value.addition(augend));
			return new ListValue(newValues);
		}
		throw new TypeException("Value " + augend + " cannot be used in this context.");
	}

	@Override
	public Value<?> subtraction(Value<?> subtrahend) throws UnsupportedOperationException, TypeException
	{
		if (subtrahend instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) subtrahend).getValue();
			if (otherValues.size() != values.size())
				throw new DimensionException("Dimension mismatch.");
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (int i = 0; i < values.size(); i++)
				newValues.add(values.get(i).subtraction(otherValues.get(i)));
			return new ListValue(newValues);
		}
		if (subtrahend instanceof NumberValue)
		{
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (Value<?> value : values)
				newValues.add(value.subtraction(subtrahend));
			return new ListValue(newValues);
		}
		throw new TypeException("Value " + subtrahend + " cannot be used in this context.");
	}

	@Override
	public Value<?> multiplication(Value<?> multiplicand) throws UnsupportedOperationException, TypeException
	{
		if (multiplicand instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) multiplicand).getValue();
			if (otherValues.size() != values.size())
				throw new DimensionException("Dimension mismatch.");
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (int i = 0; i < values.size(); i++)
				newValues.add(values.get(i).subtraction(otherValues.get(i)));
			return new ListValue(newValues);
		}
		if (multiplicand instanceof NumberValue)
		{
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (Value<?> value : values)
				newValues.add(value.multiplication(multiplicand));
			return new ListValue(newValues);
		}
		throw new TypeException("Value " + multiplicand + " cannot be used in this context.");
	}

	@Override
	public Value<?> division(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		if (divisor instanceof ListValue)
		{
			final List<Value<?>> otherValues = ((ListValue) divisor).getValue();
			if (otherValues.size() != values.size())
				throw new DimensionException("Dimension mismatch.");
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (int i = 0; i < values.size(); i++)
				newValues.add(values.get(i).subtraction(otherValues.get(i)));
			return new ListValue(newValues);
		}
		if (divisor instanceof NumberValue)
		{
			final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
			for (Value<?> value : values)
				newValues.add(value.division(divisor));
			return new ListValue(newValues);
		}
		throw new TypeException("Value " + divisor + " cannot be used in this context.");
	}

	@Override
	public Value<?> exponentiate(int exponent) throws UnsupportedOperationException, TypeException
	{
		final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
		for (Value<?> value : values)
			newValues.add(value.exponentiate(exponent));
		return new ListValue(newValues);
	}

	@Override
	public Value<?> modulo(Value<?> divisor) throws UnsupportedOperationException, TypeException
	{
		final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
		for (Value<?> value : values)
			newValues.add(value.modulo(divisor));
		throw new TypeException("Value " + divisor + " cannot be used in this context.");
	}

	@Override
	public Value<?> abs() throws UnsupportedOperationException, TypeException
	{
		final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
		for (Value<?> value : values)
			newValues.add(value.abs());
		return new ListValue(newValues);
	}

	@Override
	public Value<?> negate() throws UnsupportedOperationException, TypeException
	{
		final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
		for (Value<?> value : values)
			newValues.add(value.negate());
		return new ListValue(newValues);
	}

	@Override
	public Value<?> round() throws UnsupportedOperationException, TypeException
	{
		final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
		for (Value<?> value : values)
			newValues.add(value.round());
		return new ListValue(newValues);
	}
	
	@Override
	public Value<?> factorial() throws UnsupportedOperationException, TypeException
	{
		final ArrayList<Value<?>> newValues = new ArrayList<>(values.size());
		for (Value<?> value : values)
			newValues.add(value.factorial());
		return new ListValue(newValues);
	}

	@Override
	public boolean compare(Value<?> value, CompareType type, boolean printOut)
			throws UnsupportedOperationException, TypeException
	{
		final List<Value<?>> toCompare;
		if (value instanceof ListValue)
			toCompare = ((ListValue) value).values;
		else
			throw new IllegalArgumentException("Value " + value + " cannot be used for this operation within this context.");
		final boolean[] bools = new boolean[values.size()];
		for (int i = 0; i < values.size(); i++)
			bools[i] = values.get(i).compare(toCompare.get(i), type, false);
		if (printOut)
			Main.getPrinter().println(Arrays.toString(bools));
		for (boolean b : bools)
			if (!b)
				return false;
		return true;
	}
	
	@Override
	public List<Value<?>> getValue()
	{
		return values;
	}
	
	public int getLength()
	{
		return values.size();
	}

	@Override
	public boolean add(Value<?> e) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public void add(int index, Value<?> element) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public boolean addAll(Collection<? extends Value<?>> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public boolean addAll(int index, Collection<? extends Value<?>> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public void clear() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public boolean contains(Object o)
	{
		return values.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return values.containsAll(c);
	}

	@Override
	public Value<?> get(int index)
	{
		return values.get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return values.indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	@Override
	public Iterator<Value<?>> iterator()
	{
		return values.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return values.lastIndexOf(o);
	}

	@Override
	public ListIterator<Value<?>> listIterator()
	{
		return values.listIterator();
	}

	@Override
	public ListIterator<Value<?>> listIterator(int index)
	{
		return values.listIterator(index);
	}

	@Override
	public boolean remove(Object o) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public Value<?> remove(int index) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public boolean removeAll(Collection<?> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public boolean retainAll(Collection<?> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public Value<?> set(int index, Value<?> element) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Class is immutable.");
	}

	@Override
	public int size()
	{
		return values.size();
	}

	@Override
	public List<Value<?>> subList(int fromIndex, int toIndex)
	{
		return values.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return values.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return values.toArray(a);
	}
}
