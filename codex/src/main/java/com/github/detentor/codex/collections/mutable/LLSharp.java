package com.github.detentor.codex.collections.mutable;

import java.util.Comparator;

import com.github.detentor.codex.cat.monads.Option;
import com.github.detentor.codex.collections.AbstractMutableLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.immutable.ListSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

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
public class LLSharp<T> extends AbstractMutableLinearSeq<T, LLSharp<T>>
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
		//Compare this to Nil:
		return this.head() == null && this.tail() == null; 
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
	public String toString()
	{
		return mkString("[", ", ", "]");
	}
	
	//Overrides obrigatórios
	
	@Override
	public <B> LLSharp<B> map(final Function1<? super T, B> function)
	{
		return (LLSharp<B>) super.map(function);
	}

	@Override
	public <B> LLSharp<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		return (LLSharp<B>) super.collect(pFunction);
	}

	@Override
	public <B> LLSharp<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		return (LLSharp<B>) super.flatMap(function);
	}

	@Override
	public LLSharp<Tuple2<T, Integer>> zipWithIndex()
	{
		return (LLSharp<Tuple2<T, Integer>>) super.zipWithIndex();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public LLSharp<T> sorted()
	{
		return sorted(new DefaultComparator());
	}

	@Override
	public LLSharp<T> sorted(final Comparator<? super T> comparator)
	{
		return from(ListSharp.from(this).sorted(comparator));
	}

	/**
	 * Essa classe é um builder para SharpCollection baseado em um LinkedListSharp. MUTÁVEL
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
