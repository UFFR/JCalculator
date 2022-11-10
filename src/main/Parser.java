package main;

import static main.Main.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;

import exceptions.DimensionException;
import exceptions.SyntaxException;
import util.Function;
import util.Operation;
import util.OperationType;
import util.PeekableIterator;
import util.TextType;
import util.expressions.FactorialExpression;
import util.expressions.NestedExpression;
import util.tokens.Token;
import util.tokens.Token.TokenType;
import util.values.*;

/**
 * Primary class to parse tokens and structure them properly for evaluation.
 * @author UFFR
 *
 */
public class Parser
{
	/**{@code Map} that directs strings to functions.**/
	private static final Map<String, Operation> FUNCTION_MAP;
	/**Types of tokens that allow implicit multiplication, depending on position.**/
	private static final Set<TokenType> LEFT_IMPLICIT_TOKEN_TYPES = Sets.immutableEnumSet(TokenType.VALUE, TokenType.CLOSING_BRACE, TokenType.CLOSING_BRACKET, TokenType.CLOSING_PARENTHESIS, TokenType.EXPRESSION),
										RIGHT_IMPLICIT_TOKEN_TYPES = Sets.immutableEnumSet(TokenType.VALUE, TokenType.OPEN_BRACE, TokenType.OPEN_BRACKET, TokenType.OPEN_PARENTHESIS, TokenType.EXPRESSION);
	/**All strings that are reserved and cannot be variable names.**/
	public static final Set<String> RESERVED_SET;
	
	/**Token list for printing. Has only minor alterations from what was given to the parser.**/
	private final List<Token> originalTokens;
	/**Main {@code List} where parsing operations take place.**/
	private final List<Token> tokens;
	/**An iterator that supports peeking for ease of traversing the tokens.**/
	private final PeekableIterator<Token> iterator;
	/**The {@code Deque} where parsed values will go.**/
	private final Deque<Value<?>> values = new ArrayDeque<Value<?>>();
	/**The {@code Deque} where parsed operators will go.**/
	private final Deque<Operation> operators = new ArrayDeque<Operation>();
	
	static
	{
		final Builder<String, Operation> builder = new Builder<String, Operation>();
		for (Function function : Function.values())
			builder.put(function.toString().toLowerCase(), function);
		builder.put("asin", Function.ARCSIN);
		builder.put("acos", Function.ARCCOS);
		builder.put("atan", Function.ARCTAN);
		builder.put("acsc", Function.ARCCSC);
		builder.put("asec", Function.ARCSEC);
		builder.put("acot", Function.ARCCOT);
		FUNCTION_MAP = builder.buildOrThrow();
		
		RESERVED_SET = new ImmutableSet.Builder<String>().addAll(FUNCTION_MAP.keySet()).addAll(COMMAND_STRINGS).add("true", "false", "ans").build();
	}
	
	public Parser(List<Token> tokens)
	{
		this.tokens = preevaluate(tokens);
		originalTokens = ImmutableList.copyOf(this.tokens);
		iterator = PeekableIterator.getIterator(this.tokens);
		parse();
	}
	
	public Parser(String input)
	{
		this(TOKENIZER.apply(input));
	}
	
	/**
	 * Retrieve {@link #originalTokens}. Only has minor changes, mostly for printing out.
	 * @return An {@link ImmutableList} of the tokens.
	 */
	public List<Token> getTokens()
	{
		return originalTokens;
	}
	
	/**
	 * Retrieve the finished {@code Deque} of {@link #values}.
	 * @return A brand new {@code Deque} containing the parsed values.
	 */
	public Deque<Value<?>> getValues()
	{
		return new ArrayDeque<Value<?>>(values);
	}
	
	/**
	 * Retrieve the finished {@code Deque} of {@link #operators}
	 * @return A brand new {@code Deque} containing the parsed operators.
	 */
	public Deque<Operation> getOperators()
	{
		return new ArrayDeque<Operation>(operators);
	}

	/**
	 * Performs initial evaluation and parsing before the main method.<br>
	 * Primarily used to swap variable names for the actual values and apply implicit multiplication.
	 * @param tokensIn The tokens provided by the {@link Tokenizer}.
	 * @return A new {@code List} for the main parsing sequence.
	 */
	private static List<Token> preevaluate(List<Token> tokensIn)
	{
		// Copy list for safety.
		final List<Token> tokens = new ArrayList<Token>(tokensIn);
		for (int i = 0; i < tokens.size(); i++)
		{
			final Token token = tokens.get(i);
			// Check if variable or "Ans"
			if (token.getType() == TokenType.TEXT)
			{
				final String text = token.toString();
				if ("ans".equalsIgnoreCase(text))
				{
					if (getContext().hasLastAnswer())
					{
						tokens.remove(i);
						tokens.add(i, getContext().getLastAnswer());
					} else
						throw new SyntaxException("No last answer available.");
					continue;
				}
				// Is not a variable
				if (RESERVED_SET.contains(text.toLowerCase()))
					continue;
				// Might be a variable
				if (getContext().hasVar(text))
				{
					tokens.remove(i);
					tokens.add(i, getContext().getVar(text));
				} else
					throw new SyntaxException("Undefined variable: [" + text + ']');
			}
		}
		
		// Apply implicit multiplication
		TokenType lastType = TokenType.NULL;
		for (int i = 0; i < tokens.size(); i++)
		{
			final Token token = tokens.get(i);
			if (implicitMultiplication(lastType, token.getType()))
				tokens.add(i, OperationType.MULTIPLICATION);
			
			lastType = tokens.get(i).getType();
		}
		
		return tokens;
	}
	
	/**
	 * Checks if the two {@link TokenType}s can have implicit multiplication applied.
	 * @param previous The token previously checked.
	 * @param current The token currently being checked.
	 * @return True, if implicit multiplication may be applied, false if not.
	 */
	private static boolean implicitMultiplication(TokenType previous, TokenType current)
	{
		return LEFT_IMPLICIT_TOKEN_TYPES.contains(previous) && RIGHT_IMPLICIT_TOKEN_TYPES.contains(current);
	}
	
	// TODO Finish
	/**
	 * The primary parsing method. Contains most of the controlling logic.
	 * @throws SyntaxException If any malformed tokens or values are detected during the process.
	 */
	private void parse() throws SyntaxException
	{
		// TODO Change to something prettier
		if ((iterator.peekFirst().getType() == TokenType.OPERATOR && ((OperationType) iterator.peekFirst()) != OperationType.SUBTRACTION) || (iterator.peekLast().getType() == TokenType.OPERATOR && ((OperationType) iterator.peekLast()) != OperationType.FACTORIAL))
			throw new SyntaxException("Unmatched infix operator!");
		boolean negateLast = false;
		while (iterator.hasNext())
		{
			final Token token = iterator.next();
			switch (token.getType())
			{
				case VALUE: values.offer((Value<?>) token); break;
				case ECPHONEME:
					iterator.next();
					if (values.isEmpty())
						throw new SyntaxException("Suffix operator '!' has no associated value!");
					else
						values.offer(new FactorialExpression(values.pollLast()));
					break;
				case OPERATOR:
					// TODO Negation and precedence
					if (((iterator.hasPrevious() && iterator.peekPrevious().getType() == TokenType.OPERATOR) || (!iterator.hasPrevious() && iterator.peekNext().getType() == TokenType.VALUE)) && ((OperationType) token) == OperationType.SUBTRACTION)
					{
						negateLast = true;
						continue;
					} else
						operators.offer((Operation) token);
					break;
				case TEXT:
					final String text = token.toString();
					if (FUNCTION_MAP.containsKey(text.toLowerCase()))
					{
						if (tokens.get(iterator.nextIndex()).getType() != TokenType.OPEN_PARENTHESIS)
							throw new SyntaxException("Function [" + text + "] has no body!");
						final Operation operation = getFunction(text);
						final List<List<Token>> body = parseCSV(iterator, '(', ')');
						switch (body.size())
						{
							case 1: values.offer(operation.constructExpression(Evaluator.evaluateParsedExpression(PARSER.apply(body.get(0))), null)); break;
							case 2: values.offer(operation.constructExpression(Evaluator.evaluateParsedExpression(PARSER.apply(body.get(0))), Evaluator.evaluateParsedExpression(PARSER.apply(body.get(1))))); break;
							default: throw new SyntaxException("Function body contains an unexpected amount of values! (" + body.size() + ')');
						}
					} else if ("true".equalsIgnoreCase(text) || "false".equalsIgnoreCase(text))
						values.offer(new BooleanValue(Boolean.parseBoolean(text)));
					else if (COMMAND_STRINGS.contains(text))
						throw new SyntaxException("Command must be first value.");
					else
						throw new SyntaxException("Text is no known function name or saved variable.");
					break;
				case OPEN_BRACKET: values.offer(parseList(iterator)); break;
				case OPEN_BRACE: values.offer(parseMatrix(iterator)); break;
				case OPEN_PARENTHESIS: values.offer(parseNested(iterator)); break;
				default: throw new SyntaxException("Unknown or unexpected token: " + iterator.peekNext());
			}
			
			// If a negation operation was detected
			if (negateLast)
			{
				values.offer(values.pollLast().negate());
				negateLast = false;
			}
		}
	}
	
	/**
	 * Skip so many iterations on the given iterator. Concludes prematurely if the limit of the iterator is met first.
	 * @param iterator The iterator to skip ahead with.
	 * @param count The amount of skips.
	 */
	private static void skipIteration(PeekableIterator<Token> iterator, int count)
	{
		for (int i = 0; iterator.hasNext() && i < count; i++)
			iterator.next();
	}
	
	/**
	 * Parses nested tokens given the opening and closing characters.
	 * @param tokens The token {@code PeekableIterator}, in case it is also nested.
	 * @param opening The opening {@code char}. Must be valid in {@link TokenType.CHAR_MAP}.
	 * @param closing The closing {@code char}. Must be valid in {@link TokenType.CHAR_MAP}.
	 * @return A sub-list of tokens representing what was nested.
	 * @throws SyntaxException If no valid closing token is found.
	 */
	private static List<Token> parseGenericNested(PeekableIterator<Token> tokens, char opening, char closing) throws SyntaxException
	{
		final TokenType openingToken = TokenType.CHAR_MAP.inverse().get(opening), closingToken = TokenType.CHAR_MAP.inverse().get(closing);
		if (tokens.next().getType() != openingToken)
			throw new SyntaxException("No opening token '" + opening + "' found!");
		
		final List<Token> sublist = new ArrayList<Token>();
		int skip = 0;
		while (tokens.hasNext())
		{
			final Token token = tokens.next();
			if (token.getType() == openingToken)
				skip++;
			else if (token.getType() == closingToken)
			{
				if (skip > 0)
					skip--;
				else
					return sublist;
			} else
				sublist.add(token);
		}
		throw new SyntaxException("No closing token '" + closing + "' found!");
	}
	
	/**
	 * Attempts to parse comma separated tokens, considering nested tokens. See {@link #parseGenericNested(PeekableIterator, char, char)}.
	 * @param tokens The token {@code PeekableIterator}, in case it is also nested.
	 * @param opening The opening {@code char}. Must be valid in {@link TokenType.CHAR_MAP}.
	 * @param closing The closing {@code char}. Must be valid in {@link TokenType.CHAR_MAP}.
	 * @return A {@code List} of lists of tokens, each entry ideally being a comma separated value.
	 * @throws SyntaxException In case a nested value was detected and it didn't have a closing token.
	 */
	private static List<List<Token>> parseCSV(PeekableIterator<Token> tokens, char opening, char closing)
	{
		final List<List<Token>> lists = new ArrayList<List<Token>>();
		final PeekableIterator<Token> subIterator = PeekableIterator.getIterator(parseGenericNested(tokens, opening, closing));
		final List<Token> list = new ArrayList<Token>();
		while (subIterator.hasNext())
		{
			final Token token = subIterator.next();
			switch (token.getType())
			{
				case COMMA: lists.add(new ArrayList<Token>(list)); list.clear(); break;
				case OPEN_BRACE: list.add(parseMatrix(subIterator)); break;
				case OPEN_BRACKET: list.add(parseList(subIterator)); break;
				case OPEN_PARENTHESIS: list.add(parseNested(subIterator)); break;
				default: list.add(token); break;
			}
		}
		lists.add(list);
		return lists;
	}
	
	/**
	 * Parse the standard nested form in mathematics, separated by parenthesis.
	 * @param tokens The token iterator used by the parser.
	 * @return The parsed nested value.
	 * @throws SyntaxException If no valid closing token is found.
	 */
	private static Value<?> parseNested(PeekableIterator<Token> tokens)
	{
		return new NestedExpression(parseGenericNested(tokens, '(', ')'));
	}
	
	/**
	 * Attempt to parse a list of values, similar to {@link #parseCSV(PeekableIterator, char, char)}, separated by brackets.
	 * @param tokens The token iterator used by the parser.
	 * @return The list of values.
	 * @throws SyntaxException If no valid closing token is found.
	 */
	private static Value<List<Value<?>>> parseList(PeekableIterator<Token> tokens)
	{
		final List<List<Token>> lists = parseCSV(tokens, '{', '}');
		final List<Value<?>> values = new ArrayList<Value<?>>(lists.size());
		for (List<Token> list : lists)
			values.add(Evaluator.evaluateParsedExpression(PARSER.apply(list)));
		return new ListValue(values);
	}
	
	/**
	 * Attempt to parse a matrix, similar to a {@link #parseList(PeekableIterator)}, separated by braces. 
	 * @param tokens The token iterator used by the parser.
	 * @return The parsed matrix.
	 * @throws SyntaxException If no valid closing token is found.
	 * @throws DimensionException If the matrix has invalid dimensions.
	 */
	private static Value<Value<?>[][]> parseMatrix(PeekableIterator<Token> tokens)
	{
		final PeekableIterator<Token> subIterator = PeekableIterator.getIterator(parseGenericNested(tokens, '[', ']'));
		final List<List<Value<?>>> intermediary = new ArrayList<List<Value<?>>>();
		final Value<?>[][] matrix;
		do
		{
			final List<List<Token>> lists = parseCSV(subIterator, '[', ']');
			final List<Value<?>> values = new ArrayList<Value<?>>(lists.size());
			for (List<Token> list : lists)
				values.add(Evaluator.evaluateParsedExpression(PARSER.apply(list)));
			intermediary.add(values);
		} while (subIterator.hasNext() && subIterator.peekNext().getType() == TokenType.NEW_LINE);
		final int rows = intermediary.size(), columns = intermediary.get(0).size();
		matrix = new Value<?>[rows][columns];
		for (int r = 0; r < rows; r++)
		{
			if (intermediary.get(r).size() != columns)
				throw new DimensionException("Dimension mismatch on matrix construction.");
			for (int c = 0; c < columns; c++)
				matrix[r][c] = intermediary.get(r).get(c);
		}
		return new MatrixValue(matrix);
	}
	
	/**
	 * Gets the {@code Function} given the string. Accepts alternate names (ie asin and arcsin). Uses {@link #FUNCTION_MAP}.
	 * @param func The string name of the function.
	 * @return The technical object that represents the function.
	 * @throws SyntaxException If the string does not represent a function.
	 */
	public static Operation getFunction(String func) throws SyntaxException
	{
		if (FUNCTION_MAP.containsKey(func))
			return FUNCTION_MAP.get(func);
		else
			throw new SyntaxException("No such function: " + func);
	}
	
	public static TextType getTextType(String input)
	{
		final String lowInput = input.toLowerCase();
		if (COMMAND_STRINGS.contains(lowInput))
			return TextType.COMMAND;
		else if (FUNCTION_MAP.containsKey(lowInput))
			return TextType.FUNCTION;
		else if ("true".equals(lowInput) || "false".equals(input))
			return TextType.BOOLEAN;
		else
			return TextType.VARIABLE;
	}
	

	@Override
	public int hashCode()
	{
		return Objects.hash(operators, originalTokens, tokens, values);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Parser))
			return false;
		final Parser other = (Parser) obj;
		return Objects.equals(operators, other.operators) && Objects.equals(originalTokens, other.originalTokens)
				&& Objects.equals(tokens, other.tokens) && Objects.equals(values, other.values);
	}

	@Override
	public String toString()
	{
		final byte maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Parser [originalTokens=")
				.append(originalTokens != null ? toString(originalTokens, maxLen) : null).append(", tokens=")
				.append(tokens != null ? toString(tokens, maxLen) : null).append(", values=")
				.append(values != null ? toString(values, maxLen) : null).append(", operators=")
				.append(operators != null ? toString(operators, maxLen) : null).append(']');
		return builder.toString();
	}

	private static String toString(Collection<?> collection, byte maxLen)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append('[');
		byte i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++)
		{
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append(']');
		return builder.toString();
	}
	
}
