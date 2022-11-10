package util;

@Deprecated
public enum ParseMode
{
	NUMBER(false),
	NESTED(true),
	ABS(true),
	TEXT(false),
	STRING(true),
	LIST(true),
	MATRIX(true),
	OPERATOR(false),
	UNSET(false);
	public final boolean nested;
	private ParseMode(boolean nested)
	{
		this.nested = nested;
	}
}
