package main;

import static main.Main.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import ch.obermuhlner.math.big.DefaultBigDecimalMath;
import exceptions.SyntaxException;
import util.values.ConstantValue;
import util.values.Value;

/**
 * Contains information relevant to the current run.
 * @author UFFR
 *
 */
public class Context
{
	/**A map that stores variables. Variables may be any {@link Value}, but the key must be valid.**/
	protected final Map<String, Value<?>> varMap = new HashMap<>();
	
	/**The {@link MathContext} used by arithmetic operations, cannot be changed.**/
	protected final MathContext context;

	/**Constants calculated with the precision specified by the {@link #context} object.**/
	public final Value<BigDecimal> PI, E, PHI;
	
	/**The last value calculated. May also be stringified exceptions.**/
	protected Value<?> lastAnswer = null;
	
	/**Create a context with the specified precision.**/
	public Context(int precision)
	{
		// Unlimited precision is usually not supported by operations.
		if (precision < 1)
			throw new IllegalArgumentException("Precision level " + precision + " is not supported.");
		context = new MathContext(precision);
		DefaultBigDecimalMath.setDefaultMathContext(context);
		PI = new ConstantValue(DefaultBigDecimalMath.pi(), 'π');
		E = new ConstantValue(DefaultBigDecimalMath.e(), 'e');
		PHI = new ConstantValue(DefaultBigDecimalMath.sqrt(BigDecimal.valueOf(5)).add(BigDecimal.ONE).divide(BigDecimal.valueOf(2)), 'φ');
	}
	
	/**
	 * Attempt to assign a variable given the raw command and the specified context.
	 * @param entry Raw assignment command string.
	 * @param context The current running context of this program.
	 * @throws SyntaxException If the method detects an invalid operation.
	 */
	static void attemptAssign(String entry, Context context) throws SyntaxException
	{
		final String[] split = entry.split(":=");
		if (split.length != 2)
			throw new SyntaxException("Invalid assignment operation.");
		attemptAssign(split[0].trim(), split[1].trim(), context);
	}
	
	/**
	 * Attempt to assign a variable given the split variable name and raw string to tokenize and parse.<br>
	 * Will fail if the variable name is invalid.
	 * @param varName The variable name, will be checked before an assignment is made.
	 * @param toAssign The raw string that will be tokenized and parsed.
	 * @param context The current running context of this program.
	 */
	static void attemptAssign(String varName, String toAssign, Context context)
	{
		if (isValidVarName(varName))
			context.addVar(varName, printer.printEntry(toAssign));
		else
			getPrinter().println("Variable name is not acceptable. First letter must not be a digit, must not contain whitespace or any character that may be an operator, or be a reserved keyword.");
	}
	
	/**
	 * Checks if a potential variable name is valid.<br>
	 * Variable names must not begin with a digit, contain whitespace or any character used in operators, or be a reserved keyword.
	 * @param name The variable name to check.
	 * @return True, if the variable name is valid, false otherwise.
	 */
	public static boolean isValidVarName(String name)
	{
		if (Character.isDigit(name.charAt(0)))
			return false;
		
		for (char c : name.toCharArray())
			if (Character.isWhitespace(c) || Tokenizer.POTENTIAL_OPERATORS.contains(c))
				return false;
		
		return !Parser.RESERVED_SET.contains(name.toLowerCase());
	}
	
	/**
	 * Set the last value given by the program.
	 * @param lastAnswer The value to assign to {@link #lastAnswer}.
	 */
	public void setLastAnswer(Value<?> lastAnswer)
	{
		this.lastAnswer = lastAnswer;
	}
	
	/**
	 * Retrieve {@link #lastAnswer}.
	 * @return The last value calculated, may be {@code null}, check {@link #hasLastAnswer()} first.
	 */
	public Value<?> getLastAnswer()
	{
		return lastAnswer;
	}
	
	/**
	 * If {@link #lastAnswer} has been assigned to.
	 * @return True if {@code lastAnswer} is not {@code null}.
	 */
	public boolean hasLastAnswer()
	{
		return lastAnswer != null;
	}
	
	/**
	 * Assign a variable to the list.
	 * @param name The name of the variable, will be used as key.
	 * @param value The variable itself, will be used as value.
	 */
	protected void addVar(String name, Value<?> value)
	{
		varMap.put(name, value);
	}
	
	/**
	 * Check if the variable exists.
	 * @param name The name of the variable to check.
	 * @return True, if the variable exists, false if not.
	 */
	public boolean hasVar(String name)
	{
		return varMap.containsKey(name);
	}
	
	/**
	 * Retrieve a variable given the name.
	 * @param name The variable name to use.
	 * @return The requested variable. May be {@code null} if it doesn't exist, so check {@link #hasVar(String)} first.
	 */
	public Value<?> getVar(String name)
	{
		return varMap.get(name);
	}
	
	/**
	 * Delete a variable given the name. Does nothing if it didn't exist to begin with.
	 * @param name The variable to delete.
	 */
	protected void delVar(String name)
	{
		varMap.remove(name);
	}
	
	/**
	 * Wipe all stored variables.
	 */
	protected void clearVars()
	{
		varMap.clear();
	}
	
	/**
	 * Print all stored variables. Notes if none are stored.
	 */
	public void printVars()
	{
		getPrinter().println("Currently stored variables:\n");
		if (varMap.isEmpty())
			getPrinter().println("[No variables stored yet.]");
		else
			varMap.forEach(Context::printVar);
	}
	
	/**
	 * Prints a single variable, used for the method reference.
	 * @param name The variable's name.
	 * @param value The variable's value.
	 */
	private static void printVar(String name, Value<?> value)
	{
		getPrinter().println(name);
		getPrinter().print('=');
		getPrinter().println(String.format("%128s", value));
	}
	
	/**
	 * Retrieves the entire variable registry.
	 * @return A {@link ImmutableMap} copy of the variable registry.
	 */
	public Map<String, Value<?>> getVarMap()
	{
		return ImmutableMap.copyOf(varMap);
	}
	
}
