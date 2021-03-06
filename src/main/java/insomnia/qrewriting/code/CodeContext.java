package insomnia.qrewriting.code;

import java.util.Arrays;
import java.util.Collection;

/**
 * Contexte de codage
 * 
 * @author zuri
 */
public class CodeContext
{
	private String[] replacements;

	public CodeContext(String k)
	{
		replacements    = new String[1];
		replacements[0] = k;
	}

	/**
	 * Met replacements[0] à k
	 */
	private void setCodeNull(String k)
	{
		int i = getCodeState(k);

		if (i < 0)
			return;

		String tmp = replacements[0];
		replacements[i] = tmp;
		replacements[0] = k;
	}

	public CodeContext(String k, Collection<String> e)
	{
		replacements = new String[e.size()];
		e.toArray(replacements);
		Arrays.sort(replacements);
		setCodeNull(k);
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
		return Arrays.toString(replacements);
	}

	public int size()
	{
		return replacements.length;
	}
}
