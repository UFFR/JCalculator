package util.tokens;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * A simple token that contains only a single symbol, usually operators.
 * @author UFFR
 *
 */
public class SymbolToken implements Token
{
	/**{@code Set} for non-symbol type tokens, throws exception if one is passed to the constructor.**/
	private static final Set<TokenType> NON_SYMBOL_SET = Sets.immutableEnumSet(TokenType.EXPRESSION, TokenType.NULL, TokenType.TEXT, TokenType.VALUE);
	
	private final TokenType tokenType;
	/**
	 * Constructs a symbol token.
	 * @param tokenType The specific kind of symbol it represents.
	 * @throws IllegalArgumentException If the {@code TokenType} passed is in {@link #NON_SYMBOL_SET}, thus it does not represent a symbol.
	 */
	public SymbolToken(TokenType tokenType) throws IllegalArgumentException
	{
		if (NON_SYMBOL_SET.contains(tokenType))
			throw new IllegalArgumentException(tokenType + " is not a symbol type!");
		this.tokenType = tokenType;
	}

	@Override
	public TokenType getType()
	{
		return tokenType;
	}

	@Override
	public String toString()
	{
		return tokenType.toString();
	}
}
