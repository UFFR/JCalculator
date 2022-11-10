package util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.util.Pair;
import main.Evaluator;
import main.Main;
import main.Parser;
import main.Tokenizer;
import util.tokens.Token;
import util.values.StringValue;
import util.values.Value;

/**
 * An abstract utility class to print text, to anywhere, as specified by the implementation.
 * @author UFFR
 *
 */
public abstract class Printer implements Flushable, Closeable, IPrinter
{
	/**Supplier to create a {@link SimplePrinter}.**/
	public static final Supplier<Printer> SIMPLE_PRINTER = SimplePrinter::new;
	/**Supplier to create a {@link FilePrinter}.**/
	public static final Function<Path, Printer> FILE_PRINTER = t ->
	{
		try
		{
			return new FilePrinter(t);
		} catch (IOException e)
		{
			final Printer printer = SIMPLE_PRINTER.get();
			printer.println("The following exception was caught trying to create a FilePrinter object, falling back to SimplePrinter.");
			printer.println(e);
			return printer;
		}
	};
	/**Supplier to create a {@link BinaryFilePrinter}.**/
	public static final Function<Path, Printer> BINARY_PRINTER = BinaryFilePrinter::new;
	
	/**The string that seperates entries, autogenerated.**/
	public static final String SEPERATOR_STRING;
	
	static
	{
		final char[] seperator = new char[128];
		Arrays.fill(seperator, '━');
		SEPERATOR_STRING = new String(seperator);
	}
	
	/**Counter for entries**/
	protected int entries;
	
	@Override
	public void printException(String cause, Exception e, boolean printStack)
	{
		if (printStack)
		{
			final StringWriter stringWriter = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			println(stringWriter);
		} else
			println(e);
		Main.getContext().setLastAnswer(new StringValue(e.toString()));
	}
	
	/**
	 * Print out and evaluate the entry.
	 * @param entry The entry to be tokenized, parsed, and evaluated.
	 * @return The final result.
	 */
	public Value<?> printEntry(String entry)
	{
		final StringBuilder builder = new StringBuilder();
		final Parser parser = Main.PARSER.apply(Tokenizer.tokenize(entry));
		final Value<?> value = Evaluator.evaluateParsedExpression(parser);
		parser.getTokens().forEach(builder::append);
		incrementEntries();
		println("Entry: #" + getEntryCount());
		println(SEPERATOR_STRING);
		println(builder);
		println();
		println(String.format("%128s", value));
		println(SEPERATOR_STRING);
		return value;
	}
	
	public void incrementEntries()
	{
		entries++;
	}
	
	public int getEntryCount()
	{
		return entries;
	}
	
	/**
	 * Simplest form of the printer, only prints to {@code System.out} and nothing else.
	 * @author UFFR
	 *
	 */
	protected static class SimplePrinter extends Printer
	{

		@Override
		public void flush() throws IOException
		{
			// Unnecessary
		}

		@Override
		public void print(String string)
		{
			System.out.print(string);
		}

		@Override
		public void print(Object object)
		{
			System.out.print(object);
		}

		@Override
		public void print(char... chars)
		{
			System.out.println(chars);
		}

		@Override
		public void print(char c)
		{
			System.out.print(c);
		}

		@Override
		public void println()
		{
			System.out.println();
		}

		@Override
		public void println(String string)
		{
			System.out.println(string);
		}

		@Override
		public void println(Object object)
		{
			System.out.println(object);
		}

		@Override
		public void println(char... chars)
		{
			System.out.println(chars);
		}

		@Override
		public void println(char c)
		{
			System.out.println(c);
		}

		@Override
		public void close() throws IOException
		{
			// Unnecessary
		}
		
	}
	
	/**
	 * Prints to both {@code System.out} and a text file supplied in the constructor.
	 * @author UFFR
	 *
	 */
	protected static class FilePrinter extends Printer
	{
		private final Path outPath;
		private final OutputStream outputStream;
		public FilePrinter(Path outPath) throws IOException
		{
			this.outPath = outPath;
			outputStream = Files.newOutputStream(outPath, StandardOpenOption.CREATE);
		}

		@Override
		public void flush() throws IOException
		{
			System.out.println("Flushed buffer to " + outPath);
			outputStream.flush();
		}
		
		@Override
		public void print(String string)
		{
			System.out.print(string);
			try
			{
				outputStream.write(string.getBytes());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void print(Object object)
		{
			System.out.print(object);
			print(object.toString());
		}

		@Override
		public void print(char... chars)
		{
			final String string = new String(chars);
			print(string);
		}

		@Override
		public void print(char c)
		{
			System.out.print(c);
			try
			{
				outputStream.write(c >>> 8);
				outputStream.write(c);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void println()
		{
			print('\n');
		}

		@Override
		public void println(String string)
		{
			System.out.println(string);
			try
			{
				outputStream.write((string + '\n').getBytes());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void println(Object object)
		{
			println(object.toString());
		}

		@Override
		public void println(char... chars)
		{
			final String string = new String(chars);
			println(string);
		}

		@Override
		public void println(char c)
		{
			print(c);
			print('\n');
		}
		
		@Override
		public void close() throws IOException
		{
			outputStream.close();
		}
		
		@Override
		protected void finalize() throws Throwable
		{
			close();
		}
		
	}
	
	/**
	 * Prints to {@code System.out} but also a binary file containing the entries in standard format and a {@code List} containing the entry pairs in order.
	 * @author UFFR
	 *
	 */
	protected static class BinaryFilePrinter extends Printer
	{
		private final StringBuilder builder = new StringBuilder();
		private final List<Pair<String, Value<?>>> entryList = new ArrayList<Pair<String,Value<?>>>();
		private final Path outPath;
		
		public BinaryFilePrinter(Path outPath)
		{
			this.outPath = outPath;
		}
		
		@Override
		public void flush() throws IOException
		{
			try (final ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(outPath)))
			{
				outputStream.defaultWriteObject();
				outputStream.writeObject(Main.getContext().getVarMap());
				outputStream.writeObject(Main.getContext().getLastAnswer());
			}
		}

		@Override
		public void close() throws IOException
		{
			flush();
		}

		@Override
		public void print(String string)
		{
			builder.append(string);
		}

		@Override
		public void print(Object object)
		{
			builder.append(object);
		}

		@Override
		public void print(char... chars)
		{
			builder.append(chars);
		}

		@Override
		public void print(char c)
		{
			builder.append(c);
		}

		@Override
		public void println()
		{
			print('\n');
		}

		@Override
		public void println(String string)
		{
			print(string);
			println();
		}

		@Override
		public void println(Object object)
		{
			print(object);
			println();
		}

		@Override
		public void println(char... chars)
		{
			print(chars);
			println();
		}

		@Override
		public void println(char c)
		{
			print(c);
			println();
		}
		
		@Override
		public Value<?> printEntry(String entry)
		{
			final Value<?> value = super.printEntry(entry);
			entryList.add(new Pair<String, Value<?>>(entry, value));
			return value;
		}
		
		@Override
		public void printException(String cause, Exception e, boolean printStack)
		{
			super.printException(cause, e, printStack);
			final List<Token> tokens = Tokenizer.tokenize(cause);
			final StringBuilder builder = new StringBuilder();
			tokens.forEach(builder::append);
			entryList.add(new Pair<String, Value<?>>(builder.toString(), new StringValue(e.toString())));
		}
	}
}