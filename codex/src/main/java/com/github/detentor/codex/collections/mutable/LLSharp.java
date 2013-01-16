package com.github.detentor.codex.collections.mutable;

import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractMutableGenericCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.monads.Option;

/**
 * Implementação mutável da lista encadeada. <br/> 
 * A mutabilidade da lista é limitada: ela permite adicionar e remover elementos 'inplace' (ou seja,
 * a estrutura interna da lista é modificada depois da adição ou remoção), mas não é possível setar,
 * diretamente, o valor do head ou do tail. <br/><br/>
 * 
 *  A lista encadeada é ideal para remoção do primeiro elemento, ou adição de elementos. Note-se que a 
 *  lista encadeada adiciona elementos como uma pilha (FILO). Além disso, é a estrutura ideal para 
 *  a criação de listas (potencialmente) infinitas.
 * 
 * @author Vinicius Seufitele
 *
 * @param <T>
 */
public class LLSharp<T> extends AbstractMutableGenericCollection<T, LLSharp<T>>
{
	private T head;
	private LLSharp<T> tail;

	// O objeto vazio
	private static final LLSharp<Object> Nil = new LLSharp<Object>(null, null);

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LLSharp()
	{
		this(null, null);
	}

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	@SuppressWarnings("unchecked")
	protected LLSharp(final T theHead)
	{
		this(theHead, (LLSharp<T>) Nil);
	}

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LLSharp(final T theHead, final LLSharp<T> theTail)
	{
		head = theHead;
		tail = theTail;
	}

	/**
	 * Constrói uma instância de ListSharp vazia.
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de ListSharp vazia.
	 */
	public static <A> LLSharp<A> empty()
	{
		return new LLSharp<A>();
	}
	
	/**
	 * Cria uma nova LinkedListSharp, a partir do valor passado como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valor O valor da LinkedListSharp
	 * @return Uma nova LinkedListSharp, cujo elemento será o valor passado como parâmetro
	 */
	public static <T> LLSharp<T> from(final T valor)
	{
		return LLSharp.<T>empty().add(valor);
	}

	/**
	 * Cria uma nova LinkedListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valores A LinkedListSharp a ser criada, a partir dos valores
	 * @return Uma nova LinkedListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> LLSharp<T> from(final T... valores)
	{
		final LLSharp<T> retorno = LLSharp.empty();

		for (int i = valores.length - 1; i > -1; i--)
		{
			retorno.add(valores[i]);
		}
		return retorno;
	}

	/**
	 * Cria uma instância de LinkedListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição dos
	 * elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LLSharp<T> from(final Iterable<T> theIterable)
	{
		final LinkedListBuilder<T> builder = new LinkedListBuilder<T>();
		
		for (final T ele : theIterable)
		{
			builder.add(ele);
		}
		return builder.result();
	}

	@Override
	public boolean isEmpty()
	{
		return this.equals(Nil);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator()
	{
		final LLSharp<T>[] curEle = new LLSharp[1];
		curEle[0] = this;

		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return ! curEle[0].isEmpty();
			}

			@Override
			public T next()
			{
				final T toReturn = curEle[0].head;
				curEle[0] = curEle[0].tail;
				return toReturn;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("Operação não suportada");
			}
		};
	}

	@Override
	public T head()
	{
		return head;
	}

	@Override
	public LLSharp<T> tail()
	{
		return tail;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new LinkedListBuilder<B>();
	}

	/**
	 * {@inheritDoc}<br/>
	 */
	@Override
	public LLSharp<T> add(final T element)
	{
		final LLSharp<T> prevElement = new LLSharp<T>(this.head, this.tail);
		this.head = element;
		this.tail = prevElement;
		return this;
	}

	@Override
	public LLSharp<T> remove(final T element)
	{
		if (this.isEmpty())
		{
			return this;
		}

		if (Option.from(this.head).equals(Option.from(element)))
		{
			this.head = this.tail.head;
			this.tail = this.tail.tail;
		}
		else
		{
			this.tail = this.tail.remove(element);
		}
		return this;
	}
	
	@Override
	public LLSharp<T> addAll(final Iterable<? extends T> col)
	{
		for (T ele : col)
		{
			this.add(ele);
		}
		return this;
	}

	@Override
	public LLSharp<T> removeAll(final Iterable<T> col)
	{
		for (T ele : col)
		{
			this.remove(ele);
		}
		return this;
	}

	@Override
	public LLSharp<T> clear()
	{
		this.head = null;
		this.tail = null; 
		return this;
	}

	@Override
	public int size()
	{
		if (this.isEmpty())
		{
			return 0;
		}
		return 1 + this.tail.size();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (head == null ? 0 : head.hashCode());
		result = prime * result + (tail == null ? 0 : tail.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final LLSharp other = (LLSharp) obj;
		if (head == null)
		{
			if (other.head != null)
			{
				return false;
			}
		}
		else if (!head.equals(other.head))
		{
			return false;
		}
		if (tail == null)
		{
			if (other.tail != null)
			{
				return false;
			}
		}
		else if (!tail.equals(other.tail))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}
	
	
	/**
	 * Essa classe é um builder para SharpCollection baseado em um LinkedListSharp.
	 * 
	 * @author Vinícius Seufitele Pinto
	 *
	 * @param <E> O tipo de dados do ListSharp retornado
	 */
	private static final class LinkedListBuilder<E> implements Builder<E, SharpCollection<E>>
	{
		private LLSharp<E> list = LLSharp.empty();
		private LLSharp<E> last;

		@Override
		public void add(final E element)
		{
			if (list.isEmpty())
			{
				list = LLSharp.from(element);
				last = list;
			}
			else
			{
				final LLSharp<E> novoEle = LLSharp.from(element);
				last.tail = novoEle;
				last = novoEle;
			}
		}

		@Override
		public LLSharp<E> result()
		{
			return list;
		}
	}
	
}