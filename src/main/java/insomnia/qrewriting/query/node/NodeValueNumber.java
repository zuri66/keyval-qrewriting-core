package insomnia.qrewriting.query.node;

public class NodeValueNumber extends NodeValue
{
	private double	n;

	public NodeValueNumber(double nn)
	{
		n = nn;
	}

	public double getNumber()
	{
		return n;
	}

	@Override
	public boolean isNumber()
	{
		return true;
	}

	@Override
	public Double getValue()
	{
		return Double.valueOf(n);
	}

	@Override
	public String toString()
	{
		return "'" + n + "'";
	}

	@Override
	public NodeValueNumber clone()
	{
		return new NodeValueNumber(n);
	}
}
