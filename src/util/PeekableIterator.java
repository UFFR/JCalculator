package util;

import java.util.List;
import java.util.ListIterator;

/**
 * A specification of {@link ListIterator} that allows peeking in both directions and peeking of the first and last elements.<br>
 * Use {@link #getIterator(List)} to construct a concrete implementation.
 * @author UFFR
 *
 * @param <E> The element type of the underlying iterator/list.
 */
public interface PeekableIterator<E> extends ListIterator<E>
{
	/**
	 * Peeks the first element without changing the current index.
	 * @return The first element.
	 */
	public E peekFirst();
	/**
	 * Peeks the last element without changing the current index.
	 * @return The first element.
	 */
	public E peekLast();
	
	/**
	 * Peek the element before the current index, without changing it.
	 * @return The element before the current index.
	 */
	public E peekPrevious();
	/**
	 * Peek the element after the current index, without changing it.
	 * @return The element after the current index.
	 */
	public E peekNext();
	
	/**
	 * Gets the size of the underlying list.
	 * @return The underlying list's size.
	 */
	public int size();
	
	/**
	 * Constructs a concrete implementation of {@code PeekableIterator}.
	 * @param <T> The type of the list.
	 * @param list The list to wrap the iterator around.
	 * @return The {@code PeekableIterator} wrapping the given list.
	 */
	public static <T> PeekableIterator<T> getIterator(List<T> list)
	{
		return new PeekableIteratorImpl<T>(list);
	}
	
	static class PeekableIteratorImpl<E> implements PeekableIterator<E>
	{
		private final List<E> list;
		private final ListIterator<E> listIterator;
		public PeekableIteratorImpl(List<E> list)
		{
			this.list = list;
			listIterator = list.listIterator();
		}
		
		@Override
		public void add(E e)
		{
			listIterator.add(e);
		}

		@Override
		public boolean hasNext()
		{
			return listIterator.hasNext();
		}

		@Override
		public boolean hasPrevious()
		{
			return listIterator.hasPrevious();
		}

		@Override
		public E next()
		{
			return listIterator.next();
		}

		@Override
		public int nextIndex()
		{
			return listIterator.nextIndex();
		}

		@Override
		public E previous()
		{
			return listIterator.previous();
		}

		@Override
		public int previousIndex()
		{
			return listIterator.previousIndex();
		}

		@Override
		public void remove()
		{
			listIterator.remove();
		}

		@Override
		public void set(E e)
		{
			listIterator.set(e);
		}

		@Override
		public E peekFirst()
		{
			return list.get(0);
		}

		@Override
		public E peekLast()
		{
			return list.get(list.size() - 1);
		}

		@Override
		public E peekPrevious()
		{
			return list.get(listIterator.previousIndex());
		}

		@Override
		public E peekNext()
		{
			return list.get(listIterator.nextIndex());
		}
		
		@Override
		public int size()
		{
			return list.size();
		}
		
	}
}
