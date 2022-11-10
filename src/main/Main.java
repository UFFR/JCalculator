package main;

import java.math.MathContext;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.collect.ImmutableSet;

import util.IPrinter;
import util.Printer;
import util.tokens.Token;

public class Main
{
	/**Strings reserved for commands.**/
	public static final Set<String> COMMAND_STRINGS = ImmutableSet.of("exit", "del", "vars");
	/**A supplier type to create a {@link Parser} given tokens.**/
	public static final Function<List<Token>, Parser> PARSER = Parser::new;
	/**A supplier type to create a {@link Deque} of tokens for a {@link Parser} to take.**/
	public static final Function<String, List<Token>> TOKENIZER = Tokenizer::tokenize;
	/**Scanner to read user input.**/
	private static final Scanner SCANNER = new Scanner(System.in);
	/**Command line options.**/
	private static final Options OPTIONS = new Options();
	
	static
	{
		// TODO Proper options
		OPTIONS.addOption(Option.builder("p").longOpt("precision").desc("Digits of precision to set the program to. Higher precision will result in longer calculating time for irrational numbers, but also supports very large numbers as well. Default is 128.").required(false).hasArg(true).optionalArg(false).argName("digits").build());
		OPTIONS.addOption(Option.builder("e").longOpt("export").desc("For printing options that export to a file, set the output path.").required(false).hasArg(true).optionalArg(false).argName("path").build());
		OPTIONS.addOption(Option.builder().longOpt("print-stack-trace").desc("Print the full stack trace of exceptions, possibly useful for debugging, but usually not required for most non-developers.").required(false).hasArg(false).build());
	}
	
	/**The current program's {@link Context}.**/
	static Context context;
	/**An abstract class to print the outputs. May be to {@code System.out} only or also include a file.**/
	static Printer printer;
	/**Whether or not the print the entire stack during exceptions. Good for debugging, but superfluous for general syntax errors.**/
	static boolean printStack;
	public static void main(String[] args)
	{
		// TODO Proper switch
		printer = Printer.SIMPLE_PRINTER.get();
		int precision;
		try
		{
			final CommandLine commandLine = new DefaultParser().parse(OPTIONS, args);
			try
			{
				precision = Integer.parseInt(commandLine.getOptionValue('p', "128"));
			} catch (NumberFormatException e)
			{
				printer.println("Caught [" + e + "] trying to parse argument '-p', defaulting to precision of 128.");
				precision = 128;
			}
			printStack = commandLine.hasOption("print-stack-trace");
		} catch (ParseException e)
		{
			printer.println(e);
			System.exit(10);
			return;
		}
		
		context = new Context(precision);
		printer.println("Loaded with a precision of " + precision + '.');
		String lastInput = "";
		// Main execution loop
		do
		{
			if (!lastInput.isEmpty())
			{
				try
				{
					// TODO Add more special command functionality
					if (lastInput.contains(":="))
						Context.attemptAssign(lastInput, getContext());
					else if (lastInput.toLowerCase().startsWith("vars"))
						context.printVars();
					else
						context.setLastAnswer(printer.printEntry(lastInput));
				} catch (Exception e)
				{
					printer.printException(lastInput, e, printStack);
				}
			}
			printer.print("> ");
		} while (!(lastInput = SCANNER.nextLine().trim()).equalsIgnoreCase("exit"));
	}
	
	/**
	 * Shorthand to get the precision of {@link MathContext}'s precision.
	 * @return
	 */
	public static int getPrecision()
	{
		return context.context.getPrecision();
	}
	
	/**
	 * Get the current program's {@link Context}.
	 * @return
	 */
	public static Context getContext()
	{
		return context;
	}
	
	/**
	 * Shorthand to get the {@link MathContext} of the current program.
	 * @return
	 */
	public static MathContext getMathContext()
	{
		return context.context;
	}
	
	/**
	 * Get the printer, but passed through its interface, to remove resource leak warnings.
	 * @return The program's {@link Printer}, abstracted to {@link IPrinter}.
	 */
	public static IPrinter getPrinter()
	{
		return printer;
	}

	/**
	 * Closes the scanner and printer.
	 */
	@Override
	protected void finalize() throws Throwable
	{
		SCANNER.close();
		printer.flush();
		printer.close();
	}
	
}
