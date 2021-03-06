package com.github.detentor.codex.collections;

import java.util.Iterator;

import com.github.detentor.codex.product.Tuple2;


/**
 * Classe abstrata para sequências (definindo o contrato básico do método equals e hashcode), de modo que elas possam
 * ser comparadas de maneira uniforme entre si.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public abstract class AbstractSeq<T, U extends Seq<T>> extends AbstractSharpCollection<T, Seq<T>>
{
	//ATENÇÃO: Esse método está aqui apenas para permitir ao tipo 'U' ser acessado pelas classes
	//inferiores. Se esse método for removido, então a classe mais abaixo não vai conseguir saber
	//o tipo 'U', pois o método tail() também está definido em LinearSeq
	
	@SuppressWarnings("unchecked")
	@Override
	public U tail()
	{
		return (U) super.tail();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Tuple2<? extends U, ? extends U> splitAt(final Integer num)
	{
		return (Tuple2<U, U>) super.splitAt(num);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Seq<? extends U> grouped(final Integer size)
	{
		return (Seq<U>) super.grouped(size);
	}

	@Override
	public int hashCode()
	{
		int result = 1;

		for (Object element : this)
		{
			result = 31 * result + (element == null ? 0 : element.hashCode());
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || !(obj instanceof Seq<?>))
		{
			return false;
		}
		
		if (this instanceof IndexedSeq<?> && obj instanceof IndexedSeq<?>)
		{
			return compareIndexed((IndexedSeq<?>) this, (IndexedSeq<?>)obj);
		}

		//Não verifica o tamanho por causa da complexidade
		final Seq<?> other = (Seq<?>) obj;

		final Iterator<T> thisIte = this.iterator();
		final Iterator<?> otherIte = other.iterator();
		
		while (thisIte.hasNext() && otherIte.hasNext())
		{
			final T thisEle = thisIte.next();
			final Object otherEle = otherIte.next();
			
			if (! (thisEle == null ? otherEle == null : thisEle.equals(otherEle)))
			{
				return false;
			}
		}
		//Só retorna true se os dois iterators terminaram juntos
		return thisIte.hasNext() == otherIte.hasNext();
	}
	
	/**
	 * Verificação mais rápida para os casos de ser sequências indexadas
	 * @param iSeq1
	 * @param iSeq2
	 * @return
	 */
	private boolean compareIndexed(final IndexedSeq<?> iSeq1, final IndexedSeq<?> iSeq2)
	{
		if (iSeq1.size() != iSeq2.size())
		{
			return false;
		}

		// Verifica se os elementos são iguais
		for (int i = 0; i < iSeq1.size(); i++)
		{
			if (! (iSeq1.apply(i) == null ? iSeq2.apply(i) == null : iSeq1.apply(i).equals(iSeq2.apply(i))))
			{
				return false;
			}
		}
		return true;
	}
}
