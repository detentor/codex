package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function0;

public abstract class Arrow0<A> implements Function0<A>, Arrow
{
	@Override
	public int getArity()
	{
		return 0;
	}
}
