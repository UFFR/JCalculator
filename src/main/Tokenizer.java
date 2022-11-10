package main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;

import ch.obermuhlner.math.big.BigDecimalMath;
import exceptions.SyntaxException;
import util.CompareType;
import util.Operation;
import util.OperationType;
import util.tokens.SymbolToken;
import util.tokens.TextToken;
import util.tokens.Token;
import util.tokens.Token.TokenType;
import util.values.NumberValue;
import util.values.StringValue;
import util.values.Value;

/**
 * Main class that converts a string/chars into {@link Token}s to be parsed by the {@link Parser}.
 * @author UFFR
 */
public class Tokenizer
{
	/**{@code Map} that directs string operators to technical operator objects. Abstracted to {@link Operator} so it can include comparison operators.**/
	public static final Map<String, Operation> OPERATOR_MAP;
	/**{@code Set} of chars that may be an operator.**/
	public static final Set<Character> POTENTIAL_OPERATORS = ImmutableSet.of('+', '-', '*', '/', '%', '^', '!', '=', '<', '>', ':');
	/**{@code Map} that directs certain strings to constants, similar to how variables are handled.**/
	public static final Map<String, Value<BigDecimal>> CONSTANT_MAP;
	
	static
	{
		final Builder<String, Operation> builder = new Builder<String, Operation>();
		for (OperationType type : OperationType.values())
			builder.put(String.valueOf(type.getSymbol()), type);
		for (CompareType type : CompareType.values())
			builder.put(type.toString(), type);
		OPERATOR_MAP = builder.buildOrThrow();
		
		CONSTANT_MAP = ImmutableMap.of(
				"pi", Main.getContext().PI,
				"e", Main.getContext().E,
				"phi", Main.getContext().PHI);
	}
	
	/**The {@code List} where the detected tokens will go to.**/
	private final List<Token> tokens = new ArrayList<Token>();
	/**The original string.**/
	private final String source;
	/**The array of characters created by the {@link #source} string.**/
	private final char[] chars;
	
	/**The current read index. Available for all methods to increment as needed.**/
	private int index = 0;
	
	/**
	 * Construct a tokenizer to turn the string input into a list of tokens.
	 * @param source The string to tokenize.
	 * @throws SyntaxException If any internal method catches invalid characters.
	 */
	public Tokenizer(String source) throws SyntaxException
	{
		this.source = source;
		chars = source.toCharArray();
		tokenize();
	}
	
	public String getSource()
	{
		return source;
	}
	
	/**
	 * Main method to tokenize the characters.
	 */
	private void tokenize()
	{
		for (index = 0; index < chars.length; index++)
		{
			final char c = chars[index];
			switch (c)
			{
				case ' ': continue;
				case '(': tokens.add(new SymbolToken(TokenType.OPEN_PARENTHESIS)); break;
				case ')': tokens.add(new SymbolToken(TokenType.CLOSING_PARENTHESIS)); break;
				case '[': tokens.add(new SymbolToken(TokenType.OPEN_BRACE)); break;
				case ']': tokens.add(new SymbolToken(TokenType.CLOSING_BRACE)); break;
				case '{': tokens.add(new SymbolToken(TokenType.OPEN_BRACKET)); break;
				case '}': tokens.add(new SymbolToken(TokenType.CLOSING_BRACKET)); break;
				case '!': tokens.add(new SymbolToken(TokenType.ECPHONEME)); break;
				case ',': tokens.add(new SymbolToken(TokenType.COMMA)); break;
				case '|': tokens.add(new SymbolToken(TokenType.PIPE)); break;
				case '\n': tokens.add(new SymbolToken(TokenType.NEW_LINE)); break;
				case '"': parseString(); break;
				// Logic that can't be put into a switch
				default:
					if (Character.isDigit(c) || c == '.' || (c == '-' && (index == 0 || "+-*/^(".contains(String.valueOf(chars[index - 1])))))
						parseNumber();
					else if (Character.isLetter(c))
						parseText();
					else if (POTENTIAL_OPERATORS.contains(c))
						parseOperator();
					else
						throw new SyntaxException("Unknown or unexpected character: " + c);
					break;
			}
		}
	}
	
	/**
	 * If a quotation mark is detected, assume it's a string and build one until another quotation is detected and assume it's a closing.
	 */
	private void parseString()
	{
		final StringBuilder builder = new StringBuilder();
		// Initial ++ is to skip the first quotation
		for (++index; index < chars.length; index++)
		{
			final char c = chars[index];
			if (c == '"')
				break;
			else
				builder.append(c);
		}
		if (chars[chars.length - 1] != '"')
			throw new SyntaxException("Unmatched closing parenthesis!");
		tokens.add(new StringValue(builder.toString()));
	}
	
	/**
	 * If a digit or basic operator is detected, begin to check for digits and build a BigDecimal from them.
	 */
	private void parseNumber()
	{
		final StringBuilder builder = new StringBuilder();
		int last = index;
		boolean isDecimal = false;
		while (last < chars.length && (Character.isDigit(chars[last]) || chars[last] == '.'))
		{
			builder.append(chars[last]);
			if (chars[last] == '.' && !isDecimal)
				isDecimal = true;
			else if (chars[last] == '.' && isDecimal)
				throw new SyntaxException("Number cannot contain more than one decimal place.");
			last++;
		}
		// Special negation case
		if (builder.length() == 0 && chars[last] == '-')
		{
			tokens.add(OperationType.SUBTRACTION);
			return;
		}
		final BigDecimal decimal = BigDecimalMath.toBigDecimal(builder.toString());
		index = last - 1;
		tokens.add(new NumberValue(decimal));
	}
	
	/**
	 * If the char is a symbol found in {@link #POTENTIAL_OPERATORS}, assume is an operator.
	 * @throws SyntaxException If the parsed string does not correlate to any valid operator.
	 */
	private void parseOperator() throws SyntaxException
	{
		final StringBuilder builder = new StringBuilder(2);
		int last = index;
		while (last < chars.length && POTENTIAL_OPERATORS.contains(chars[last]))
			builder.append(chars[last++]);
		index = last - 1;
		tokens.add(getOperation(builder.toString()));
	}
	
	/**
	 * Text not wrapped in quotes, may refer to variables, constants, functions, etc.
	 */
	private void parseText()
	{
		final StringBuilder builder = new StringBuilder();
		int last = index;
		while (last < chars.length && Character.isLetter(chars[last]))
			builder.append(chars[last++]);
		index = last - 1;
		final String text = builder.toString();
		tokens.add(CONSTANT_MAP.containsKey(text.toLowerCase()) ? CONSTANT_MAP.get(text.toLowerCase()) : new TextToken(text));
	}
	
	/**
	 * Query {@link #OPERATOR_MAP} given the string to request the {@link OperationType} it correlates to.
	 * @param op The string version of the operator.
	 * @return The operator object abstracted to the {@code Operation} type.
	 * @throws SyntaxException If the string does not represent any valid operator.
	 */
	public static Operation getOperation(String op) throws SyntaxException
	{
		if (OPERATOR_MAP.containsKey(op))
			return OPERATOR_MAP.get(op);
		else
			throw new SyntaxException("Token is not operator: " + op);
	}
	
	/**
	 * Get the finished token list after the main method has finished.
	 * @return The finished token list.
	 */
	public List<Token> getTokens()
	{
		return new ArrayList<Token>(tokens);
	}
	
	/**
	 * Shorthand way to tokenize text and retrieve the tokens.
	 * @param input The string input to go to the constructor {@link #Tokenizer(String)}.
	 * @return The finished token list as specified by {@link #getTokens()}.
	 */
	public static List<Token> tokenize(String input)
	{
		return new Tokenizer(input).getTokens();
	}
}
