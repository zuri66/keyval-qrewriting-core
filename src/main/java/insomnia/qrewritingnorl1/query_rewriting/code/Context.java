package insomnia.qrewritingnorl1.query_rewriting.code;

import java.util.Arrays;
import java.util.Collection;

/**
 * Contexte de codage
 * 
 * @author zuri
 * 
 */
public class Context
{
	private String[] replacements;

	public Context(String k)
	{
		replacements = new String[1];
		replacements[0] = k;
	}

	public Context(String k, Collection<String> e)
	{
		this(k);
		replacements = new String[e.size()];
		e.toArray(replacements);
	}

	public String[] getReplacements()
	{
		return replacements;
	}

	public String getK(int codeState)
	{
		return replacements[codeState];
	}

	/**
	 * 
	 * @param k
	 * @return l'index ou une valeur négative si rien n'est trouvé
	 */
	public int getCodeState(String k)
	{
		return Arrays.binarySearch(replacements, k);
	}

	@Override
	public String toString()
	{
		return "" + replacements;
	}

	public int size()
	{
		return replacements.length;
	}
}