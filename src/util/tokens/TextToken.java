package util.tokens;

/**
 * A token representing a non-string piece of text.<br>
 * May in turn represent a variable, function, etc.
 * @author UFFR
 *
 */
public class TextToken implements Token
{
	private final String text;
	public TextToken(String text)
	{
		this.text = text;
	}

	@Override
	public TokenType getType()
	{
		return TokenType.TEXT;
	}

	@Override
	public String toString()
	{
		return text;
	}
}
