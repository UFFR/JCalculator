package util.tokens;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * The simplest form of data used by the program. Generated by a {@link Tokenizer} for use by a {@link Parser}.
 * @author UFFR
 *
 */
public interface Token
{
	/**
	 * A specifier for the type of token. Not tokens in themselves.
	 * @author UFFR
	 *
	 */
	public enum TokenType
	{
		TEXT,
		VALUE,
		OPERATOR,
		COMPARATOR,
		EXPRESSION,
		ECPHONEME,
		QUOTE,
		PIPE,
		COMMA,
		OPEN_PARENTHESIS,
		CLOSING_PARENTHESIS,
		OPEN_BRACKET,
		CLOSING_BRACKET,
		OPEN_BRACE,
		CLOSING_BRACE,
		NEW_LINE,
		NULL;
		
		public static final BiMap<TokenType, Character> CHAR_MAP;
		
		static
		{
			CHAR_MAP = ImmutableBiMap.of(
					CLOSING_BRACE, ']',
					CLOSING_BRACKET, '}',
					CLOSING_PARENTHESIS, ')',
					ECPHONEME, '!',
					QUOTE, '"',
					COMMA, ',',
					OPEN_BRACE, '[',
					OPEN_BRACKET, '{',
					OPEN_PARENTHESIS, '(',
					NEW_LINE, '\n');
		}
		
		public char getCharacter()
		{
			return CHAR_MAP.getOrDefault(this, '\u0000');
		}
		
		@Override
		public String toString()
		{
			final char c = getCharacter();
			return c == 0 ? super.toString() : String.valueOf(c);
		}
	}
	
	@Override
	public String toString();
	
	public TokenType getType();
}