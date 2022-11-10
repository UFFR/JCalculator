package util;

import static java.math.MathContext.DECIMAL128;
import static main.Main.getMathContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import com.google.common.annotations.Beta;

import ch.obermuhlner.math.big.BigDecimalMath;
import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Experimental class for extra precision, combining a {@link BigInteger} for the integral part of a number and a {@link BigDecimal} specifically for the decimal or fractional part of a number.
 * @author UFFR
 *
 */
@Beta
@Immutable
public class HybridNumber extends Number implements Comparable<HybridNumber>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7027016052928883845L;
	private final BigInteger integerPart;
	private final BigDecimal decimalPart;
	public HybridNumber(String number)
	{
		final String[] split = number.trim().split(".");
		if (split.length > 2)
			throw new NumberFormatException("Cannot contain more than one decimal place.");
		integerPart = new BigInteger(split[0]);
		decimalPart = split.length == 2 ? new BigDecimal('.' + split[1]) : BigDecimal.ZERO;
	}
	
	public HybridNumber(BigInteger integer, BigDecimal decimal)
	{
		final BigDecimal[] parts = decimal.abs().divideAndRemainder(BigDecimal.ONE);
		integerPart = integer.add(parts[0].toBigIntegerExact());
		decimalPart = parts[1];
	}
	
	public HybridNumber(BigInteger integer)
	{
		integerPart = integer;
		decimalPart = BigDecimal.ZERO;
	}
	
	public HybridNumber(BigDecimal decimal)
	{
		final BigDecimal[] parts = decimal.divideAndRemainder(BigDecimal.ONE);
		integerPart = parts[0].toBigIntegerExact();
		decimalPart = parts[1];
	}
	
	public HybridNumber addition(HybridNumber augend)
	{
		final BigInteger integral = integerPart.add(augend.integerPart);
		final BigDecimal decimal = decimalPart.add(augend.decimalPart, getMathContext());
		
		return new HybridNumber(integral, decimal);
	}
	
	public HybridNumber subtraction(HybridNumber subtrahend)
	{
		final BigInteger integer = integerPart.subtract(subtrahend.integerPart);
		final BigDecimal decimal = decimalPart.subtract(subtrahend.decimalPart, getMathContext());
		
		return new HybridNumber(integer, decimal);
	}
	
	public HybridNumber multiplication(HybridNumber multiplicand)
	{
		final BigInteger integer = integerPart.multiply(multiplicand.integerPart);
		final BigDecimal decimal = decimalPart.multiply(multiplicand.decimalPart, getMathContext());
		
		return new HybridNumber(integer, decimal);
	}
	
	public HybridNumber division(HybridNumber divisor)
	{
		final BigInteger integer = integerPart.divide(divisor.integerPart);
		final BigDecimal decimal = decimalPart.divide(divisor.decimalPart, getMathContext());
		
		return new HybridNumber(integer, decimal);
	}
	
	public HybridNumber exponentiation(int exponent)
	{
		final BigInteger integer = integerPart.pow(exponent);
		final BigDecimal decimal = decimalPart.pow(exponent, getMathContext());
		
		return new HybridNumber(integer, decimal);
	}

	public HybridNumber modulo(HybridNumber divisor)
	{
		final BigInteger integer = integerPart.mod(divisor.integerPart);
		final BigDecimal decimal = decimalPart.remainder(divisor.decimalPart, getMathContext());
		
		return new HybridNumber(integer, decimal);
	}
	
	public HybridNumber abs()
	{
		return new HybridNumber(integerPart.abs(), decimalPart);
	}
	
	public HybridNumber negate()
	{
		return new HybridNumber(integerPart.negate(), decimalPart);
	}
	
	public HybridNumber round()
	{
		return new HybridNumber(integerPart, decimalPart.round(getMathContext()));
	}
	
	public HybridNumber factorial()
	{
		final BigInteger integer = BigDecimalMath.factorial(integerPart.intValueExact()).toBigIntegerExact();
		final BigDecimal decimal = BigDecimalMath.factorial(decimalPart, getMathContext());
		
		return new HybridNumber(integer, decimal);
	}
	
	@Override
	public int compareTo(HybridNumber o)
	{
		return integerPart.compareTo(o.integerPart) & decimalPart.compareTo(o.decimalPart);
	}
	
	@Override
	public double doubleValue()
	{
		final BigInteger rounded = integerPart.add(decimalPart.round(DECIMAL128).toBigIntegerExact());
		if (rounded.compareTo(BigDecimal.valueOf(Double.MAX_VALUE).toBigInteger()) > 0)
			return Double.MAX_VALUE;
		else if (rounded.compareTo(BigDecimal.valueOf(Double.MIN_VALUE).round(DECIMAL128).toBigIntegerExact()) < 0)
			return Double.MIN_VALUE;
		else
			return rounded.doubleValue();
	}

	@Override
	public float floatValue()
	{
		final BigInteger rounded = integerPart.add(decimalPart.round(DECIMAL128).toBigIntegerExact());
		if (rounded.compareTo(BigDecimal.valueOf(Float.MAX_VALUE).toBigInteger()) > 0)
			return Float.MAX_VALUE;
		else if (rounded.compareTo(BigDecimal.valueOf(Float.MIN_VALUE).round(DECIMAL128).toBigIntegerExact()) < 0)
			return Float.MIN_VALUE;
		else
			return rounded.floatValue();
	}

	@Override
	public int intValue()
	{
		final BigInteger rounded = integerPart.add(decimalPart.round(DECIMAL128).toBigIntegerExact());
		if (rounded.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0)
			return Integer.MAX_VALUE;
		else if (rounded.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0)
			return Integer.MIN_VALUE;
		else
			return rounded.intValueExact();
	}

	@Override
	public long longValue()
	{
		final BigInteger rounded = integerPart.add(decimalPart.round(DECIMAL128).toBigIntegerExact());
		if (rounded.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
			return Long.MAX_VALUE;
		else if (rounded.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
			return Long.MIN_VALUE;
		else
			return rounded.longValueExact();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(decimalPart, integerPart);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof HybridNumber))
			return false;
		final HybridNumber other = (HybridNumber) obj;
		return Objects.equals(decimalPart, other.decimalPart) && Objects.equals(integerPart, other.integerPart);
	}

	@Override
	public String toString()
	{
		return integerPart.toString() + decimalPart.stripTrailingZeros().toPlainString().substring(1);
	}

}
