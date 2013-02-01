package com.github.detentor.codex.collections;

import java.util.Comparator;

/**
 * As classes que assinam esta interface possuem diversas funções que utilizam comparação. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface ComparisonFunctions<T>
{
	/**
	 * Retorna o valor mínimo desta coleção, desde que esta coleção possua elementos que assinem
	 * a interface Comparator.
	 * @return O elemento com o menor valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	 */
	T min();
	
	/**
	 * Retorna o valor máximo desta coleção, desde que esta coleção possua elementos que assinem
	 * a interface Comparator.
	 * @return O elemento com o maior valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	 */
	T max();
	
	/**
	 * Retorna o valor máximo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return O elemento com o maior valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 */
	T maxWith(final Comparator<? super T> comparator);
	
	/**
	 * Retorna o valor mínimo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return O elemento com o menor valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 */
	T minWith(final Comparator<? super T> comparator);

}
