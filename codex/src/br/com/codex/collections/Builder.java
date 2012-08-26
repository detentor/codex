package br.com.codex.collections;

/**
 * Builder é uma interface que determina como as coleções serão criadas. <br/>
 * A partir do builder é possível, nos métodos de ordem superior, retornar instâncias de diferentes tipos
 * de coleções (set, mapas, etc). <br/> <br/>
 * 
 * Com builders é possível determinar exatamente o tipo de objeto que será criado quando uma função
 * de ordem superior for chamada. Por exemplo, é possível passar um builder de LinkedHashSet, que assegurará
 * a ordem de inclusão dos elementos.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de elementos que serão alimentados no builder
 * @param <To> O tipo de coleção a ser retornada pelo builder
 */
public interface Builder<E, To extends Iterable<E>>
{
	/**
	 * Adiciona o elemento passado como parâmetro no Builder
	 * @param element O elemento a ser adicionado no builder
	 */
	public void add(final E element);
	
	/**
	 * Retorna a coleção criada pelo Builder.
	 * @return A coleção criada pelo Builder
	 */
	public To result();

}
