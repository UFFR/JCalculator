package util.values;

import java.math.BigDecimal;

/**
 * A specification of {@link NumberValue} for constants like pi.<br>
 * Changes no behavior other than {@link #toString()}.
 * @author UFFR
 *
 */
public class ConstantValue extends NumberValue
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5056188299666473846L;
	private final char symbol;
	public ConstantValue(BigDecimal number, char symbol)
	{
		super(number);
		this.symbol = symbol;
	}

	public ConstantValue(String number, char symbol)
	{
		super(number);
		this.symbol = symbol;
	}

	@Override
	public String toString()
	{
		return String.valueOf(symbol);
	}
}
